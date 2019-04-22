package com.gwd.entity;

/**
 * @program: Luckydraw
 * @description:
 * @author: ChenYu
 * @create: 2019-04-22 01:46
 **/
public class DrawLuckResult {
    private int id;
    private String name;
    private String document;
    private String keyWord;
    private String startTime;
    private String endTime;
    private String resultTime;
    private Integer winnerNum;
    private String award;
    private String filterType;
    private String awardResult;

    public DrawLuckResult() {
    }

    public DrawLuckResult(String name, String document, String keyWord, String startTime, String endTime, String resultTime, Integer winnerNum, String award, String filterType, String awardResult) {
        this.name = name;
        this.document = document;
        this.keyWord = keyWord;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
        this.winnerNum = winnerNum;
        this.award = award;
        this.filterType = filterType;
        this.awardResult = awardResult;
    }

    @Override
    public String toString() {
        return "DrawLuckResult{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", document='" + document + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", resultTime='" + resultTime + '\'' +
                ", winnerNum=" + winnerNum +
                ", award='" + award + '\'' +
                ", filterType='" + filterType + '\'' +
                ", awardResult='" + awardResult + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public Integer getWinnerNum() {
        return winnerNum;
    }

    public void setWinnerNum(Integer winnerNum) {
        this.winnerNum = winnerNum;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getAwardResult() {
        return awardResult;
    }

    public void setAwardResult(String awardResult) {
        this.awardResult = awardResult;
    }
}
