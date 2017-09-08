package com.lld360.cnc.repository;

import com.lld360.cnc.dto.InviteCardDto;
import com.lld360.cnc.model.InviteCard;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InviteCardDao {

    void create(InviteCard inviteCard);

    void update(InviteCard inviteCard);

    void delete(Long id);

    InviteCardDto getByCode(String inviteCode);

    List<InviteCardDto> inviteCardList(Map<String,Object> params);

    long count(Map<String,Object> params);

}
