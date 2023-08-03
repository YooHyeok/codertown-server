package io.codertown.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private Integer allPage;
    private Integer curPage;
    private Integer startPage;
    private Integer endPage;

    @JsonIgnore
    private PageRequest pageRequest;

    /**
     * PageNation에 사용되는 메소드이다. <br/>
     * 현재 페이지값과 정렬컬럼, 정렬기준 을 매개변수로 받는다 <br/>
     * PageRequest객체를 생성하고, 현재 클래스인 PageInfo 객체의 값을 초기화하고 반환한다. <br/>
     * 초기화 되는 필드는 curPage(현재페이지)와, pageRequest 필드이다. <br/>
     * # 반환된 PageInfo 객체는 아래와 같이 사용된다. <br/>
     * 1. 소유하고있는 PageRequest를 추출하여 Spring Data에 의해 데이터를 조회할때 페이지네이션 데이터를 함께 반환한다. <br/>
     * 2. 현재 페이지 정보들을 Client에 정보로 제공한다.
     * @param page
     * @param sortColumn
     * @param sortDirection
     * @return
     */
    public PageInfo createPageRequest(Integer page, String sortColumn, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page - 1, 12, Sort.by(setSortDriection(sortDirection), sortColumn));
        PageInfo pageInfo = PageInfo.builder().curPage(page).pageRequest(pageRequest).build();
        return pageInfo;
    }

    /**
     * 매개변수에 의해 정렬 기준이 결정된다. <br/>
     * createPageRequset에서 호출된다. <br/>
     * PageRequest에 사용되는 Sort.by의 Sort.Direction에서 사용될 메소드이다. <br/>
     * sortDirection 값이 DESC이면 내림차순정렬인 DESC를 반환한다. <br/>
     * sortDirection 값이 ASC이거나 NULL이거나 다른 값이면 오름차순 정렬인 ASC를 반환한다.
     * @param sortDirection
     * @return Sort.Direction.ASC | Sort.Direction.DESC
     */
    public Sort.Direction setSortDriection(String sortDirection) {
        return sortDirection == "DESC" ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    /**
     * [매개변수 1] Page<T> pages <br/>
     *  - 페이지네이션 이후 해당 Page 인터페이스타입 객체로부터 최대 페이지 정보를 매개변수로 받는다. <br/>
     * [매개변수 2] PageInfo pageInfo <br/>
     *  - 페이지네이션에 사용되었던 PageInfo 객체를 매개변수로 받는다. <br/>
     * # 각 매개변수를 통해 allPage, startPage, endPage를 계산해 내고 현재 객체의 필드들을 초기화한다.
     * @param pages
     * @param pageInfo
     * @param <T>
     */
    public <T> void setPageInfo(Page<T> pages, PageInfo pageInfo) {
        int maxPage = pages.getTotalPages();
        int curPage = pageInfo.getCurPage();
        int startPage = 0;
        int endPage = 0;
        if (curPage % 10 == 0){ //현재 페이지가 10으로 나눴을때 나머지가 0인 경우는 10의 배수이기 때문.
            startPage = pageInfo.getCurPage()/10*10-9;
            endPage = curPage;   //10, 20, 30, 40
        }else {
            startPage = pageInfo.getCurPage()/10*10+1;
            endPage = startPage+10 -1;   //10, 20, 30, 40
        }
        if(endPage>maxPage) endPage = maxPage;
        this.allPage = maxPage;
        this.startPage = startPage;
        this.endPage = endPage;
    }
}
