package io.codertown.web.entity;

import io.codertown.web.entity.project.Project;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "project")
public class ProjectPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_PART_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_NO")
    private Project project;

    @OneToOne
    @JoinColumn(name = "PART_NO")
    private Part part;

    @OneToMany(mappedBy = "projectPart")
    private List<UserProject> userProjects = new ArrayList<>(); //참여자 목록
    private int recruitCount; // 모집 인원
    private int currentCount; // 지원 인원

    public ProjectPart createProjectPart(Project project, int recruitCount, Part part) {
        return ProjectPart.builder()
                .part(part)
                .recruitCount(recruitCount)
                .project(project) //이곳에서 양방향으로 주입된다.
                .build();
    }

    public void increaseUserCount() {
        int resultCount  = this.currentCount + 1;
        if (resultCount > recruitCount) {
            throw new RuntimeException("모집 인원보다 토탈 인원이 큽니다.");
        }
        this.currentCount = resultCount;
    }

    public void decreaseUserCount() {
        int resultCount = this.currentCount - 1;
        if (resultCount < 0) {
            throw new RuntimeException("토탈 인원이 0보다 작을수 없습니다.");
        }
        this.currentCount = resultCount;
    }

    /**
     * ProjectPart 모집인원 변경감지 메소드 <br/>
     * ProjectPart의 모집인원을 수정한다
     * @param recruitCount
     */
    public void updateProjectPart(int recruitCount) {
        this.recruitCount = recruitCount;
    }
}
