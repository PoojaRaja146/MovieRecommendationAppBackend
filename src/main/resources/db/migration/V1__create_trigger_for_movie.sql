CREATE TABLE movie_audit (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             movie_id INT NOT NULL,
                             operation VARCHAR(10) NOT NULL,
                             operation_time DATETIME NOT NULL,
                             user_id VARCHAR(100) NOT NULL,
                             old_data TEXT,
                             new_data TEXT
);

DELIMITER $$

CREATE TRIGGER tr_movie_insert
    AFTER INSERT ON movie
    FOR EACH ROW
BEGIN
    INSERT INTO movie_audit (movie_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (NEW.id, 'INSERT', NOW(), user(), NULL, CONCAT_WS('|', NEW.id, NEW.created_at, NEW.language, NEW.name, NEW.updated_at, NEW.actor_id, NEW.director_id, NEW.genre_id));
    END$$


CREATE TRIGGER tr_movie_update
    AFTER UPDATE ON movie
    FOR EACH ROW
BEGIN
    INSERT INTO movie_audit (movie_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (OLD.id, 'UPDATE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.created_at, OLD.language, OLD.name, OLD.updated_at, OLD.actor_id, OLD.director_id, OLD.genre_id), CONCAT_WS('|', NEW.id, NEW.created_at, NEW.language, NEW.name, NEW.updated_at, NEW.actor_id, NEW.director_id, NEW.genre_id));
END$$


CREATE TRIGGER tr_movie_delete
    AFTER DELETE ON movie
    FOR EACH ROW
BEGIN
    INSERT INTO movie_audit (movie_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (OLD.id, 'DELETE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.created_at, OLD.language, OLD.name, OLD.updated_at, OLD.actor_id, OLD.director_id, OLD.genre_id), NULL);
END$$


DELIMITER ;
