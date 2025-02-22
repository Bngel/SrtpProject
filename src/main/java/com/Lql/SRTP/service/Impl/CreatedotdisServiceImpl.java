package com.Lql.SRTP.service.Impl;

import com.Lql.SRTP.dao.CreatedotdisDao;
import com.Lql.SRTP.dao.WarehouseLayoutDao;
import com.Lql.SRTP.entity.Dot;
import com.Lql.SRTP.entity.Dotdis;
import com.Lql.SRTP.entity.Shelves;
import com.Lql.SRTP.entity.ShelvesDis;
import com.Lql.SRTP.service.ICreatedotdisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CreatedotdisServiceImpl implements ICreatedotdisService {
    @Autowired
    private CreatedotdisDao CreatedotdisMapper;
    @Autowired
    private WarehouseLayoutDao WarehouseLayoutMapper;

    @Override
    public List<Dot> createDot() {
        Integer id = 0, shelves = 0, x, y;
        Integer lenx = 100, leny = 100;//仓库的最大xy
        List<Shelves> slist = WarehouseLayoutMapper.getAllshelves();
        //点阵
        for (int i = 0; i < lenx; i++) {
            {
                for (int j = 0; j < leny; j++) {
                    x = i;
                    y = j;
                    shelves = 0;
                    Dot dottem = new Dot(id, shelves, x, y);
                    CreatedotdisMapper.adddot(dottem);
                    id = id + 1;
                }
            }
        }
        //障碍物
        for (int i = 0; i < slist.size(); i++) {
            int x1 = slist.get(i).getSx1();
            int x2 = slist.get(i).getSx2();
            int y1 = slist.get(i).getSy1();
            int y2 = slist.get(i).getSy2();
            int sid = slist.get(i).getId();
            for (int j = x1; j < x2; j++) {
                for (int k = y1; k < y2; k++) {
                    Dot dottem1 = new Dot(null, -1, j, k);
                    CreatedotdisMapper.changedot(dottem1);
                }
            }
            //这里设置货架出库点为x2y2点，有变化再改
            Dot dottem2 = new Dot(null, sid, x2, y2);
            CreatedotdisMapper.changedot(dottem2);
        }
        List<Dot> dotlist = CreatedotdisMapper.getdotlist();
        return dotlist;
    }

    @Override
    public void adddotdatabase() {
        Integer lenx = 100, leny = 100;//仓库的最大xy
        //把相连的点保存到距离矩阵中
        for (int i = 0; i < lenx; i++) {
            for (int j = 0; j < leny; j++) {
                Dot dotnow = CreatedotdisMapper.getdot(i, j);
                if (dotnow.getShelves() != -1)//自己不是障碍物
                {
                    if (CreatedotdisMapper.getdot(i + 1, j).getShelves() != -1) {
                        Dotdis tem1 = new Dotdis(i, j, i + 1, j, 1);
                        CreatedotdisMapper.adddis(tem1);
                    } else {
                        Dotdis tem1 = new Dotdis(i, j, i + 1, j, -1);
                        CreatedotdisMapper.adddis(tem1);
                    }
                    if (CreatedotdisMapper.getdot(i, j + 1).getShelves() != -1) {
                        Dotdis tem2 = new Dotdis(i, j, i, j + 1, 1);
                        CreatedotdisMapper.adddis(tem2);
                    } else {
                        Dotdis tem2 = new Dotdis(i, j, i, j + 1, -1);
                        CreatedotdisMapper.adddis(tem2);
                    }
                }
            }
        }
    }

    @Override
    public void fyld() {
        //创建顶点list
        List<Dot> a = CreatedotdisMapper.getdotlist();
        Dotdis[][] matrixA = new Dotdis[a.size()][a.size()];
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.size(); j++) {
                Integer x1 = a.get(i).getX();
                Integer y1 = a.get(i).getY();
                Integer x2 = a.get(j).getX();
                Integer y2 = a.get(j).getY();
                Dotdis temp = new Dotdis(x1, y1, x2, y2, null);
                Dotdis temdotdis = CreatedotdisMapper.getdis(temp);
                matrixA[i][j] = temdotdis;
            }
        }
        //创建前驱顶点list
        int[][] pre = new int[a.size()][a.size()];
        //对pre数组初始化,注意存放的是前驱顶点的下标
        for (int i = 0; i < a.size(); i++) {
            Arrays.fill(pre[i], i);
        }
        // 弗洛伊德算法
        //保存距离
        int len;
        //对中间顶点遍历
        for (int k = 0; k < matrixA.length; k++) {
            //从i顶点开始出发
            for (int i = 0; i < matrixA.length; i++) {
                //到达j顶点
                for (int j = 0; j < matrixA.length; j++) {
                    //求出从i顶点出发经过k到达j的距离
                    len = matrixA[i][k].getDis() + matrixA[k][j].getDis();
                    //若len小于dis[i][j],则进行更新
                    if (len < matrixA[i][j].getDis()) {
                        //更新距离
                        matrixA[i][j].setDis(len);
                        //更新前驱顶点
                        pre[i][j] = pre[k][j];
                    }
                }
            }
        }
        //加入所有点到数据库
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.size(); j++) {
                CreatedotdisMapper.changedotdis(matrixA[i][j]);
            }
        }
    }

    @Override
    public List<ShelvesDis> getShelvesdis() {
        //查找货架之间距离以及其他数据，存入距离表
        List<Shelves> shelves = WarehouseLayoutMapper.getAllshelves();
        List<ShelvesDis> shelvesDis = new ArrayList<>();
        for (int i = 0; i < shelves.size(); i++) {
            Integer x1 = shelves.get(i).getSx2();
            Integer y1 = shelves.get(i).getSy2();
            Integer x2;
            Integer y2;
            Integer s1 = shelves.get(i).getId();
            Integer s2;
            Integer g1 = shelves.get(i).getPid();
            Integer g2;
            Double num1 = null;
            Double num2 = null;
            Double score1 = null;
            Double score2 = null;
            Integer dis;
            for (int j = i; j < shelves.size(); j++) {
                x2 = shelves.get(j).getSx2();
                y2 = shelves.get(j).getSy2();
                s2 = shelves.get(j).getId();
                g2 = shelves.get(j).getPid();
                Dotdis temdis = new Dotdis(x1, y1, x2, y2, null);
                Dotdis temdis2 = CreatedotdisMapper.getdis(temdis);
                dis = temdis2.getDis();
                ShelvesDis tem = new ShelvesDis(x1, y1, x2, y2, s1, s2, g1, g2, num1, num2, score1, score2, dis);
                CreatedotdisMapper.addshelves(tem);
            }
        }
        //根据货架获取货架对应距离接口
        shelvesDis = CreatedotdisMapper.getshelvesdis();
        return shelvesDis;
    }

}