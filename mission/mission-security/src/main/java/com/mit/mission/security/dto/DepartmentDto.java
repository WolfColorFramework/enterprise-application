package com.mit.mission.security.dto;

import com.mit.mission.security.domain.Department;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentDto extends Department implements Comparable<DepartmentDto> {

    private static final long serialVersionUID = 1L;
    private List<String> parentIdList;
    private List<DepartmentDto> children;
    private String label;
    private String value;

    @Override
    public int compareTo(DepartmentDto departmentDto) {
        Integer sort = departmentDto.getSort();
        if (sort == null) {
            sort = 0;
        }
        if (this.getSort() == null) {
            this.setSort(0);
        }
        return this.getSort().compareTo(sort);
    }
}
