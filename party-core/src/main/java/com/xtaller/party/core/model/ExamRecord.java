package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;

import java.io.Serializable;

/**
 * Created by Taller on 2018/09/11
 */
@SuppressWarnings("serial")
@TableName(value = "exam_record")
public class ExamRecord extends Model<ExamRecord> {
    private String id = IdKit.getId(this.getClass());
    private String examId;
    private String questionId;
    private String number;
    private Integer situation;
    private String answerContent;
    private Integer ordered;
    private String optionOrdered;
    private String creator;
    private Integer createTime;
    private String reviser;
    private Integer reviseTime;
    private Integer isDel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSituation() {
        return situation;
    }

    public void setSituation(Integer situation) {
        this.situation = situation;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public Integer getOrdered() {
        return ordered;
    }

    public void setOrdered(Integer ordered) {
        this.ordered = ordered;
    }

    public String getOptionOrdered() {
        return optionOrdered;
    }

    public void setOptionOrdered(String optionOrdered) {
        this.optionOrdered = optionOrdered;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getReviser() {
        return reviser;
    }

    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    public Integer getReviseTime() {
        return reviseTime;
    }

    public void setReviseTime(Integer reviseTime) {
        this.reviseTime = reviseTime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Override
    protected Serializable pkVal() {
        return id;
    }
}
