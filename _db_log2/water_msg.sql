
-- 2021.11.01
ALTER TABLE `water_msg_message`
DROP COLUMN `topic_id`,
DROP INDEX `IX_topic`;

ALTER TABLE `water_msg_message_all`
DROP COLUMN `topic_id`,
DROP INDEX `IX_topic`;
