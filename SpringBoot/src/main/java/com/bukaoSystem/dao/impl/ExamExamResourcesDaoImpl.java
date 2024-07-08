package com.bukaoSystem.dao.impl;

import com.bukaoSystem.dao.ExamExamResourcesDao;
import com.bukaoSystem.exception.ForeignKeyConstraintViolationException;
import com.bukaoSystem.model.ExamExamResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExamExamResourcesDaoImpl implements ExamExamResourcesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ExamExamResources> rowMapper = new BeanPropertyRowMapper<>(ExamExamResources.class);

    //得到试卷详情
    @Override
    public List<ExamExamResources> getAllExamExamResources() {
        String sql = "SELECT * FROM exam_exam_resources";
        return jdbcTemplate.query(sql, rowMapper);
    }
    //根据id得到试卷详情
    @Override
    public List<ExamExamResources> getExamExamResourcesById(Long id) {
        String sql = "SELECT * FROM exam_exam_resources WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }
    //根据试卷id得到试卷资源id，课程id详情
    @Override
    public List<ExamExamResources> getExamExamResourcesByExamId(Long examId) {
        String sql = "SELECT * FROM exam_exam_resources WHERE examId = ?";
        return jdbcTemplate.query(sql, rowMapper, examId);
    }
    //根据资源id得到哪些试卷有该资源（试卷id，课程id）
    @Override
    public List<ExamExamResources> getExamExamResourcesByResourceId(Long resourceId) {
        String sql = "SELECT * FROM exam_exam_resources WHERE resourceId = ?";
        return jdbcTemplate.query(sql, rowMapper, resourceId);
    }
    //根据试卷id得到试卷详情（资源id，课程id）
    @Override
    public void saveExamExamResources(ExamExamResources examExamResources) {
        if (examExamResources.getCreateTime()==null){
            String sql = "INSERT INTO exam_exam_resources (examId, resourceId) VALUES (?, ?)";
            jdbcTemplate.update(sql, examExamResources.getExamId(), examExamResources.getResourceId());
        }else {
            String sql = "INSERT INTO exam_exam_resources (examId, resourceId, createTime) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, examExamResources.getExamId(), examExamResources.getResourceId(), examExamResources.getCreateTime());
        }
    }
    //更新
    @Override
    public void updateExamExamResources(ExamExamResources examExamResources) {
        String sql = "UPDATE exam_exam_resources SET createTime = ? WHERE examId = ? AND resourceId = ?";
        jdbcTemplate.update(sql, examExamResources.getCreateTime(), examExamResources.getExamId(), examExamResources.getResourceId());
    }
    //删除
    @Override
    public void deleteExamExamResources(Long examId, Long resourceId) {
        String sql = "DELETE FROM exam_exam_resources WHERE examId = ? AND resourceId = ?";
        jdbcTemplate.update(sql, examId, resourceId);

        try {
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyConstraintViolationException("无法删除examId: " + examId + "resourceId: " + resourceId + "的信息，该id下有关联信息。");
        }
    }

}
