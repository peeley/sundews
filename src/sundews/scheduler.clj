(ns sundews.scheduler
  (:require [clojurewerkz.quartzite.scheduler :as scheduler]
            [clojurewerkz.quartzite.jobs :as jobs :refer [defjob]]
            [clojurewerkz.quartzite.triggers :as triggers]
            [clojurewerkz.quartzite.schedule.simple :refer [schedule with-interval-in-seconds repeat-forever]]
            [sundews.db :as db]
            [java-time :as java-time]
            [mount.core :refer [defstate]]))

(defstate job-scheduler
  :start (scheduler/start (scheduler/initialize))
  :stop (scheduler/shutdown job-scheduler))

(def cleanup-job-key (jobs/key "jobs.cleanup.1"))

(def cleanup-trigger-key (triggers/key "triggers.cleanup.1"))

(defjob CleanupJob
  [ctx]
  (db/delete-links-created-before! db/db (-> (java-time/instant) java-time/instant->sql-timestamp)))

(def job
  (jobs/build (jobs/of-type CleanupJob)
              (jobs/with-identity cleanup-job-key)))

(def trigger
  (triggers/build (triggers/with-identity cleanup-trigger-key)
                  (triggers/start-now)
                  (triggers/with-schedule (schedule (repeat-forever)
                                                    (with-interval-in-seconds 1)))))
(defstate cleanup-job
  :start (scheduler/schedule job-scheduler job trigger)
  :stop (scheduler/delete-job job-scheduler cleanup-job-key))

(comment
  (scheduler/pause-job job-scheduler (jobs/key cleanup-job-key))
  (scheduler/resume-job job-scheduler (jobs/key cleanup-job-key))
  )
