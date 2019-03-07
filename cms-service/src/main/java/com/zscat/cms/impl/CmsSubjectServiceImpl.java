package  com.zscat.cms.impl;

import com.github.pagehelper.PageHelper;
import com.zscat.cms.mapper.CmsSubjectMapper;
import com.zscat.cms.model.CmsSubject;
import com.zscat.cms.model.CmsSubjectExample;
import com.zscat.cms.service.CmsSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商品专题Service实现类
 * Created by zscat on 2018/6/1.
 */
@Service("redisService")
public class CmsSubjectServiceImpl implements CmsSubjectService {
    @Autowired
    private CmsSubjectMapper subjectMapper;

    @Override
    public List<CmsSubject> listAll() {
        return subjectMapper.selectByExample(new CmsSubjectExample());
    }

    @Override
    public List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CmsSubjectExample example = new CmsSubjectExample();
        CmsSubjectExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andTitleLike("%" + keyword + "%");
        }
        return subjectMapper.selectByExample(example);
    }

    @Override
    public CmsSubject selectByPrimaryKey(Long id){
        return subjectMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CmsSubject> selectByExample(CmsSubjectExample example){
        return  subjectMapper.selectByExample(example);
    }
}
