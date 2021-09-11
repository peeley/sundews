-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name create-link! :<! :1
-- :doc creates a new shortlink
INSERT INTO links
(link, user_id)
VALUES (:link, :user_id) RETURNING id

-- :name get-link-by-id :? :1
-- :doc retrieves link by id
SELECT * FROM links where id = :id

-- :name get-links :? :*
-- :doc retrieves all links
SELECT * FROM links

-- :name delete-link-by-id! :! :n
-- :doc deletes link with corresponding id
DELETE FROM links WHERE id = :id
