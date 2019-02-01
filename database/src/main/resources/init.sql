-- liquibase formatted sql

-- changeset tree:init:create_prefecture_table
create table prefecture (
  prefecture_cd char(2) comment '都道府県コード'
  ,prefecture_name varchar(191) comment '都道府県名'
  ,primary key (prefecture_cd)
) engine=innodb charset=utf8mb4 comment='都道府県';
-- rollback drop table if exists prefecture;

-- changeset tree:init:load_data_prefecture
load data local infile "src/main/resources/init/prefecture.csv" into table prefecture character set 'utf8' fields terminated by ',' enclosed by '"' lines terminated by '\n' ignore 1 lines;
-- rollback truncate prefecture;

-- changeset tree:init:create_address_table
create table address (
  prefecture_cd char(2) comment '都道府県コード'
  ,prefecture_name varchar(191) comment '都道府県名'
  ,city_cd char(5) comment '市区町村コード'
  ,city_name varchar(191) comment '市区町村名'
  ,town_street_cd char(12) comment '大字町丁目コード'
  ,town_street_name varchar(191) comment '大字町丁目名'
  ,latitude double comment '緯度'
  ,longitude double comment '経度'
  ,origin_doc_cd char(1) comment '原典資料コード'
  ,town_street_kbn_cd char(1) comment '大字・字・丁目区分コード'
  ,primary key (prefecture_cd, city_cd, town_street_cd)
  ,foreign key(prefecture_cd) references prefecture(prefecture_cd)
  ,key idx1(latitude, longitude)
) engine=innodb charset=utf8mb4 comment='住所';
-- rollback drop table if exists address;

-- changeset tree:init:load_data_address
load data local infile "src/main/resources/init/tokyo.csv" into table address character set 'utf8' fields terminated by ',' enclosed by '"' lines terminated by '\n' ignore 1 lines;
load data local infile "src/main/resources/init/kanagawa.csv" into table address character set 'utf8' fields terminated by ',' enclosed by '"' lines terminated by '\n' ignore 1 lines;
-- rollback truncate address;

-- changeset tree:init:role
create table role (
  role_cd smallint unsigned not null comment '役割コード',
  role_name varchar(191) not null comment '役割名',
  created_at datetime not null default current_timestamp comment '作成日時',
  updated_at datetime not null default current_timestamp on update current_timestamp comment '更新日時',
  primary key (role_cd),
  unique key uq1 (role_name)
) engine=innodb charset=utf8mb4 comment='役割'
-- rollback drop table if exists role;

-- changeset tree:init:insert_role
insert into role (role_cd, role_name) values
(1, 'ADMIN'),
(2, 'STAFF')
;
-- rollback truncate role;

-- changeset tree:init:user
create table user (
  user_id bigint unsigned not null auto_increment comment 'ユーザid',
  user_name varchar(191) not null comment 'ユーザ名',
  user_password varchar(191) not null comment 'パスワード',
  role_cd smallint unsigned not null comment '役割コード',
  credentials_expired_flg boolean not null default false comment '権限期限切れフラグ',
  account_expired_flg boolean not null default false comment 'アカウント期限切れフラグ',
  account_locked_flg boolean not null default false comment 'アカウント停止フラグ',
  enabled_flg boolean not null default true comment '有効フラグ',
  created_at datetime not null default current_timestamp comment '作成日時',
  updated_at datetime not null default current_timestamp on update current_timestamp comment '更新日時',
  primary key (user_id),
  unique key uq1 (user_name),
  foreign key(role_cd) references role(role_cd)
) engine=innodb charset=utf8mb4 comment='ユーザ'
-- rollback drop table if exists user;

-- changeset tree:init:insert_user
insert into user (user_name, user_password, role_cd) values
('admin', '$2a$10$qko2Zkiq8ik3yz4CfDfe1ONgE38YbCfNHGDH5j6LwLhfw20BmdpjS', 1),
('staff', '$2a$10$paJXdP6tzGfljGL.1N6eNedQB3dd7z0Oz.96/XNIWhhGOvKbvEzC6', 2)
;
-- rollback truncate user;
