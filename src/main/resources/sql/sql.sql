# Part 샘플 데이터 추가
insert into part values(null, "PM");
insert into part values(null, "디자이너");
insert into part values(null, "퍼블리셔");
insert into part values(null, "프론트엔드");
insert into part values(null, "백엔드");


select
    recruit0_.recruit_no as recruit_2_8_0_,
    recruit0_.first_reg_date as first_re3_8_0_,
    recruit0_.last_mod_date as last_mod4_8_0_,
    recruit0_.content as content5_8_0_,
    recruit0_.link as link6_8_0_,
    recruit0_.user_no as user_no8_8_0_,
    recruit0_.title as title7_8_0_,
    recruit0_1_.object_week as object_w1_1_0_,
    recruit0_1_.project_no as project_3_1_0_,
    recruit0_2_.location as location1_4_0_,
    recruit0_.category as category1_8_0_
from
    recruit recruit0_
        left outer join
    cokkiri recruit0_1_
    on recruit0_.recruit_no=recruit0_1_.recruit_no
        left outer join
    mammoth recruit0_2_
    on recruit0_.recruit_no=recruit0_2_.recruit_no
where
        recruit0_.recruit_no=3;