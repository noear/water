--2021.10.19

ALTER TABLE `water_exam_log_bcf`
    MODIFY COLUMN `cmd_arg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数' AFTER `cmd_sql`;

ALTER TABLE `water_exam_log_sql`
    MODIFY COLUMN `cmd_arg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数' AFTER `cmd_sql`;




