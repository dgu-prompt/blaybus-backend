INSERT INTO leader_quest_progress (
    quest_progress_id, frequency_type, period, status, updated_at, quest_id, department_id, employee_number
) VALUES
      (1, 'MONTH', 1,'MAX', NOW(), 1, '음성 1센터',2021030101),
      (2, 'MONTH', 2, 'MEDIUM', NOW(), 1, '음성 1센터',2021030101),
      (3, 'MONTH', 2, 'MEDIUM', NOW(), 1, '음성 1센터',2023010101),
      (4, 'MONTH', 1, 'MAX', NOW(), 1, '음성 1센터',2023010101),
      (5, 'MONTH', 2, 'MEDIUM', NOW(), 1, '음성 1센터',2023010102),
      (6, 'MONTH', 1, 'MAX', NOW(), 2, '음성 1센터', 2023010102),
      (7, 'MONTH', 2, 'MEDIUM', NOW(), 2, '음성 1센터',2021030101),
      (8, 'MONTH', 2, 'MEDIUM', NOW(), 2, '음성 1센터',2023010101),
      (9, 'MONTH', 2, 'MAX', NOW(), 2, '음성 1센터',2023010102);

INSERT INTO leader_quest (
    quest_id, description, frequency_type, max_condition, max_exp_do, median_condition, median_exp_do, quest_title, updated_at, department_id
) VALUES
      (1, '4회 이상', 'MONTH', '4회 이상', 100, '2회 이상', 50, '월특근', '2025-01-11 12:27:55.000000', '음성 1센터'),
      (2, '목적(why)-과정(how)-결과(what) 에 대한 정리', 'MONTH', '업무프로세스 개선 리드자', 67, '업무프로세스 개선 참여자', 33, '업무개선', '2025-01-11 12:27:55.000000', '음성 1센터');

INSERT INTO project (employee_number, project_name, description, project_exp_do, updated_at, project_date)
VALUES
    (2023010101, '물류센터 최적화 시스템 개발', '물류센터 내 재고 관리 시스템의 효율화를 목표로 한 프로젝트', 12, CURRENT_TIMESTAMP, '2024-06-15'),
    (2023010102, '물류센터 최적화 시스템 개발', '물류센터 내 재고 관리 시스템의 효율화를 목표로 한 프로젝트', 12, CURRENT_TIMESTAMP, '2024-06-15'),
    (2023010102, '물류센터 안전 개선 작업', '물류센터의 작업 환경을 개선하여 안전 사고를 줄이는 프로젝트', 12, CURRENT_TIMESTAMP, '2024-09-05'),
    (2018020101, '물류센터 안전 개선 작업', '물류센터의 작업 환경을 개선하여 안전 사고를 줄이는 프로젝트', 12, CURRENT_TIMESTAMP, '2024-09-05'),
    (2021030101, '창고 자동화 시스템 구축', '자동화된 창고 관리 시스템 구축을 위한 프로젝트', 6, CURRENT_TIMESTAMP, '2024-09-20'),
    (2018020101, '창고 자동화 시스템 구축', '자동화된 창고 관리 시스템 구축을 위한 프로젝트', 6, CURRENT_TIMESTAMP, '2024-09-20');

INSERT INTO job_quest_progress (
    quest_id,
    job_group_id,
    department_id,
    status,
    updated_at,
    period,
    frequency_type
)
VALUES
    (1, 1, '음성 2센터', 'PENDING', NOW(), 1, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 2, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 3, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 4, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 5, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 6, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 7, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 8, 'WEEK'),
    (1, 1, '음성 2센터', 'PENDING', NOW(), 9, 'WEEK'),
    (1, 1, '음성 2센터', 'PENDING', NOW(), 10, 'WEEK'),
    (1, 1, '음성 2센터', 'PENDING', NOW(), 11, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 12, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 13, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 14, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 15, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 16, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 17, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 18, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 19, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 20, 'WEEK'),
    (1, 1, '음성 2센터', 'PENDING', NOW(), 21, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 22, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 23, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 24, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 25, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 26, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 27, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 28, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 29, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 30, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 31, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 32, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 33, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 34, 'WEEK'),
    (1, 1, '음성 2센터', 'PENDING', NOW(), 35, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 36, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 37, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 38, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 39, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 40, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 41, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 42, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 43, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 44, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 45, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 46, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 47, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 48, 'WEEK'),
    (1, 1, '음성 2센터', 'MEDIUM', NOW(), 49, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 50, 'WEEK'),
    (1, 1, '음성 2센터', 'MAX', NOW(), 51, 'WEEK'),
    (1, 1, '음성 2센터', 'PENDING', NOW(), 52, 'WEEK');

INSERT INTO post (
    post_id,
    created_at,
    post_title,
    updated_at,
    view_count
)
VALUES
    (1,NOW(),'AAA 프로젝트 신설 (경험치 500 do, 신청 마감 ~10/31)',NOW(),0),
    (2,NOW(),'잡초이스 공고(신청 마감 ~11/20)',NOW(),0);
