package com.Lql.SRTP.entity;

import java.io.Serializable;
import java.util.Objects;

public class Shelves implements Serializable {
    private Integer id;
    private Integer sx1;
    private Integer sx2;
    private Integer sy1;
    private Integer sy2;
    private Double sv;
    private Integer pid;

    @Override
    public String toString() {
        return "Shelves{" +
                "id=" + id +
                ", sx1=" + sx1 +
                ", sx2=" + sx2 +
                ", sy1=" + sy1 +
                ", sy2=" + sy2 +
                ", sv=" + sv +
                ", pid=" + pid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelves shelves = (Shelves) o;
        return Objects.equals(id, shelves.id) && Objects.equals(sx1, shelves.sx1) && Objects.equals(sx2, shelves.sx2) && Objects.equals(sy1, shelves.sy1) && Objects.equals(sy2, shelves.sy2) && Objects.equals(sv, shelves.sv) && Objects.equals(pid, shelves.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sx1, sx2, sy1, sy2, sv, pid);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSx1() {
        return sx1;
    }

    public void setSx1(Integer sx1) {
        this.sx1 = sx1;
    }

    public Integer getSx2() {
        return sx2;
    }

    public void setSx2(Integer sx2) {
        this.sx2 = sx2;
    }

    public Integer getSy1() {
        return sy1;
    }

    public void setSy1(Integer sy1) {
        this.sy1 = sy1;
    }

    public Integer getSy2() {
        return sy2;
    }

    public void setSy2(Integer sy2) {
        this.sy2 = sy2;
    }

    public Double getSv() {
        return sv;
    }

    public void setSv(Double sv) {
        this.sv = sv;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Shelves(Integer id, Integer sx1, Integer sx2, Integer sy1, Integer sy2, Double sv, Integer pid) {
        this.id = id;
        this.sx1 = sx1;
        this.sx2 = sx2;
        this.sy1 = sy1;
        this.sy2 = sy2;
        this.sv = sv;
        this.pid = pid;
    }
}
