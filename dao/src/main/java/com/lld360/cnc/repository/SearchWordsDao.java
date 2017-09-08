package com.lld360.cnc.repository;


import com.lld360.cnc.model.SearchWords;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface SearchWordsDao {
    List<SearchWords> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    SearchWords find(Long id);

    List<String> findHotWords(int count);

    boolean hasWord(String name);

    void create(SearchWords search);

    void update(SearchWords search);

    void createOrAddCounts(String word);

    void addCounts(String name);

    void delete(Long id);

    int doBatchClearZero(Integer[] idsStrIntegerArray);

    int doOneClearZero(Integer id);

}
