create table if not exists ${logger}
(
    log_id Int64,
    trace_id Nullable(String),
    level Int32 default 0,
    tag Nullable(String),
    tag1 Nullable(String),
    tag2 Nullable(String),
    tag3 Nullable(String),
    tag4 Nullable(String),
    group Nullable(String),
    app_name Nullable(String),
    class_name Nullable(String),
    thread_name Nullable(String),
    content Nullable(String),
    from Nullable(String),
    log_date Nullable(Int32),
    log_fulltime Int64
)
engine = MergeTree PARTITION BY log_id
ORDER BY log_id;