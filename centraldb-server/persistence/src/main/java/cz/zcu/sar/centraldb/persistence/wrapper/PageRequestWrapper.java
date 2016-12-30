package cz.zcu.sar.centraldb.persistence.wrapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Matej Lochman on 21.12.16.
 */

public class PageRequestWrapper {
    private Map<String, String> queryParams;
    private PaginationParams paginationParams;

    public PageRequestWrapper() { }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public PaginationParams getPaginationParams() {
        return paginationParams;
    }

    public void setPaginationParams(PaginationParams paginationParams) {
        this.paginationParams = paginationParams;
    }

    public class PaginationParams {
        private Integer limit;
        private Integer page;
        private List<String> sort;

        public PaginationParams() { }

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
