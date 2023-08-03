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

    public PageInfo createPageRequest(Integer page, String sortColumn, String sortDirection) {
        PageRequest pageRequest = sortDirection == "DESC" ?
                PageRequest.of(page - 1, 12, Sort.by(Sort.Direction.DESC, sortColumn))
                :
                PageRequest.of(page - 1, 12, Sort.by(Sort.Direction.ASC, sortColumn));
        PageInfo pageInfo = PageInfo.builder().curPage(page).pageRequest(pageRequest).build();
        return pageInfo;

    }
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
