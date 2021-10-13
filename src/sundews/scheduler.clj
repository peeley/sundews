(ns sundews.scheduler
  (:require [clojurewerkz.quartzite.scheduler :as scheduler]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.jobs :as jobs]
            [clojurewerkz.quartzite.triggers :as triggers]
            [clojurewerkz.quartzite.schedule.simple :refer [schedule with-interval-in-seconds repeat-forever]]
            [sundews.db :as db]
            [java-time :as java-time]))

(def cleanup-scheduler (scheduler/start (scheduler/initialize)))

(def cleanup-job-key "jobs.cleanup.1")

(def cleanup-trigger-key "triggers.cleanup.1")

(defjob CleanupJob
  [ctx]
  (db/delete-links-created-before! db/db (java-time/instant)))

(def job
  (jobs/build (jobs/of-type CleanupJob)
              (jobs/with-identity (jobs/key cleanup-job-key))))

(def trigger
  (triggers/build (triggers/with-identity (triggers/key cleanup-trigger-key))
                  (triggers/start-now)
                  (triggers/with-schedule (schedule (repeat-forever)
                                                    (with-interval-in-seconds 1)))))

(comment
  (scheduler/schedule cleanup-scheduler job trigger)
  (scheduler/pause-job cleanup-scheduler cleanup-job-key)
  (java-time/instant)

  (db/get-all-links db/db)
  (db/insert-link! db/db "abc.com")
  (db/delete-links-created-before! db/db (java-time/instant))
  )
