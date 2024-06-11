-- Create the director_audit table
CREATE TABLE director_audit (
                                director_id INT NOT NULL,
                                operation VARCHAR(10) NOT NULL,
                                operation_time DATETIME NOT NULL,
                                user_id VARCHAR(50) NOT NULL,
                                old_data VARCHAR(500),
                                new_data VARCHAR(500),
                                PRIMARY KEY (director_id, operation, operation_time)
);

-- Create triggers for the director table
DELIMITER $$

CREATE TRIGGER tr_director_insert
    AFTER INSERT ON director
    FOR EACH ROW
BEGIN
    INSERT INTO director_audit (director_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (NEW.id, 'INSERT', NOW(), USER(), NULL, CONCAT_WS('|', NEW.id, NEW.created_at, NEW.gender, NEW.name, NEW.updated_at));
END$$

CREATE TRIGGER tr_director_update
    AFTER UPDATE ON director
    FOR EACH ROW
BEGIN
    INSERT INTO director_audit (director_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (OLD.id, 'UPDATE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.created_at, OLD.gender, OLD.name, OLD.updated_at), CONCAT_WS('|', NEW.id, NEW.created_at, NEW.gender, NEW.name, NEW.updated_at));
END$$

CREATE TRIGGER tr_director_delete
    AFTER DELETE ON director
    FOR EACH ROW
BEGIN
    INSERT INTO director_audit (director_id, operation, operation_time, user_id, old_data, new_data)
    VALUES (OLD.id, 'DELETE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.created_at, OLD.gender, OLD.name, OLD.updated_at), NULL);
END$$

DELIMITER ;