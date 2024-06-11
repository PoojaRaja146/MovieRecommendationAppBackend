-- Create the genre_audit table
CREATE TABLE genre_audit (
                             genre_id INT NOT NULL,
                             operation VARCHAR(10) NOT NULL,
                             operation_time DATETIME NOT NULL,
                             user_id VARCHAR(50) NOT NULL,
                             old_data VARCHAR(500),
                             new_data VARCHAR(500),
                             PRIMARY KEY (genre_id, operation, operation_time)
);

-- Create triggers for the genre table
DELIMITER $$

CREATE TRIGGER tr_genre_insert
    AFTER INSERT ON genre
    FOR EACH ROW
BEGIN
    INSERT INTO genre_audit (genre_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (NEW.id, 'INSERT', NOW(), USER(), NULL, CONCAT_WS('|', NEW.id, NEW.created_at, NEW.name, NEW.updated_at));
END$$

CREATE TRIGGER tr_genre_update
    AFTER UPDATE ON genre
    FOR EACH ROW
BEGIN
    INSERT INTO genre_audit (genre_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (OLD.id, 'UPDATE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.created_at, OLD.name, OLD.updated_at), CONCAT_WS('|', NEW.id, NEW.created_at, NEW.name, NEW.updated_at));
END$$

CREATE TRIGGER tr_genre_delete
    AFTER DELETE ON genre
    FOR EACH ROW
BEGIN
    INSERT INTO genre_audit (genre_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (OLD.id, 'DELETE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.created_at, OLD.name, OLD.updated_at), NULL);
END$$

DELIMITER ;