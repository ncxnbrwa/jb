package cn.iimedia.jb.http.bean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by iiMedia on 2018/5/21.
 */

public class SearchDatabase extends DataSupport {
    private long id;
    private String searchName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    //保存防止重复
    public static void saveNotRepeat(String searchName) {
        List<SearchDatabase> list = DataSupport.where("searchName=?", searchName)
                .find(SearchDatabase.class);
        if (list.isEmpty()) {
            SearchDatabase database = new SearchDatabase();
            database.setSearchName(searchName);
            database.save();
        } else {
            for (SearchDatabase item : list) {
                DataSupport.delete(SearchDatabase.class, item.id);
            }
            SearchDatabase database = new SearchDatabase();
            database.setSearchName(searchName);
            database.save();
        }
    }

    @Override
    public String toString() {
        return "SearchDatabase{" +
                "id=" + id +
                ", searchName='" + searchName + '\'' +
                '}';
    }
}
