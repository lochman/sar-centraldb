package cz.zcu.sar.centraldb.persistence.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Matej Lochman on 21.12.16.
 */

public class PageRequestWrapper {
    private Map<String, String> queryParams;
    private PagingParams pagingParams;

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public PagingParams getPagingParams() {
        return pagingParams;
    }

    public void setPagingParams(PagingParams pagingParams) {
        this.pagingParams = pagingParams;
    }

    class PagingParams {
        private Integer limit;
        private Integer page;
        private List<String> sort;

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public List<String> getSort() {
            return sort;
        }

        public void setSort(List<String> sort) {
            this.sort = sort;
        }
    }
}
