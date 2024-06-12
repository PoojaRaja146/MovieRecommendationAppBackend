-- Create the user_audit table
CREATE TABLE IF NOT EXISTS user_audit (
                            user_id INT NOT NULL,
                            operation VARCHAR(10) NOT NULL,
                            operation_time DATETIME NOT NULL,
                            user_id_audit VARCHAR(50) NOT NULL,
                            old_data VARCHAR(500),
                            new_data VARCHAR(500),
                            PRIMARY KEY (user_id, operation, operation_time)
);

-- Create triggers for the user table
DELIMITER $$

CREATE TRIGGER tr_user_insert
    AFTER INSERT ON user
    FOR EACH ROW
BEGIN
    INSERT INTO user_audit (user_id, operation, operation_time, user_id_audit, old_data, new_data)
    VALUES (NEW.id, 'INSERT', NOW(), USER(), NULL, CONCAT_WS('|', NEW.id, NEW.email, NEW.name, NEW.created_at, NEW.updated_at));
    END$$

    CREATE TRIGGER tr_user_update
        AFTER UPDATE ON user
        FOR EACH ROW
    BEGIN
        INSERT INTO user_audit (user_id, operation, operation_time, user_id_audit, old_data, new_data)
        VALUES (OLD.id, 'UPDATE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.email, OLD.name, OLD.created_at, OLD.updated_at), CONCAT_WS('|', NEW.id, NEW.email, NEW.name, NEW.created_at, NEW.updated_at));
        END$$

        CREATE TRIGGER tr_user_delete
            AFTER DELETE ON user
            FOR EACH ROW
        BEGIN
            INSERT INTO user_audit (user_id, operation, operation_time, user_id_audit, old_data, new_data)
            VALUES (OLD.id, 'DELETE', NOW(), USER(), CONCAT_WS('|', OLD.id, OLD.email, OLD.name, OLD.created_at, OLD.updated_at), NULL);
            END$$

            DELIMITER ;