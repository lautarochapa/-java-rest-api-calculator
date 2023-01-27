package com.coralogix.calculator.model;

public class WorldTimeServiceResponse {

    private String abbreviation;
    private String client_ip;
    private String datetime;
    private int day_of_week;
    private int day_of_year;
    private Boolean dst;
    private Boolean dst_from;
    private int dst_offset;
    private int dst_until;
    private int raw_offset;
    private String timezone;
    private int unixtime;
    private String utc_datetime;
    private String utc_offset;
    private int week_number;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getDay_of_year() {
        return day_of_year;
    }

    public void setDay_of_year(int day_of_year) {
        this.day_of_year = day_of_year;
    }

    public Boolean getDst() {
        return dst;
    }

    public void setDst(Boolean dst) {
        this.dst = dst;
    }

    public Boolean getDst_from() {
        return dst_from;
    }

    public void setDst_from(Boolean dst_from) {
        this.dst_from = dst_from;
    }

    public int getDst_offset() {
        return dst_offset;
    }

    public void setDst_offset(int dst_offset) {
        this.dst_offset = dst_offset;
    }

    public int getDst_until() {
        return dst_until;
    }

    public void setDst_until(int dst_until) {
        this.dst_until = dst_until;
    }

    public int getRaw_offset() {
        return raw_offset;
    }

    public void setRaw_offset(int raw_offset) {
        this.raw_offset = raw_offset;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(int unixtime) {
        this.unixtime = unixtime;
    }

    public String getUtc_datetime() {
        return utc_datetime;
    }

    public void setUtc_datetime(String utc_datetime) {
        this.utc_datetime = utc_datetime;
    }

    public String getUtc_offset() {
        return utc_offset;
    }

    public void setUtc_offset(String utc_offset) {
        this.utc_offset = utc_offset;
    }

    public int getWeek_number() {
        return week_number;
    }

    public void setWeek_number(int week_number) {
        this.week_number = week_number;
    }
}
