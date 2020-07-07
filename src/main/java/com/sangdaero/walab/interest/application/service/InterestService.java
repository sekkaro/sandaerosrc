package com.sangdaero.walab.interest.application.service;


import com.sangdaero.walab.common.entity.InterestCategory;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.dto.InterestName;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserInterestRepository;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InterestService {

    private InterestRepository mInterestRepository;
    private UserInterestRepository mUserInterestRepository;

    public InterestService(InterestRepository interestRepository, UserInterestRepository mUserInterestRepository) {
        this.mInterestRepository = interestRepository;
        this.mUserInterestRepository = mUserInterestRepository;
    }

    public Long addInterest(InterestDto interestDto) {
        String name = interestDto.getName().trim();
//        name = name.replaceAll(" " , "");
//        name = name.replaceAll("\\p{Z}", "");
        interestDto.setName(name);
        interestDto.setOn_off((byte)1);
        return mInterestRepository.save(interestDto.toEntity()).getId();
    }

    public List<InterestDto> getInterestList(int type) {
//        List<InterestName> interestNames = mInterestRepository.findAllByOrderByName();
        List<InterestCategory> interestCategories = new ArrayList<>();;

        if(type==1) {
            interestCategories = mInterestRepository.findAll();
        } else if(type==2) {
            interestCategories = mInterestRepository.findAllByOn_offEquals((byte)1);
        }

//        List<InterestCategory> interestCategories = mInterestRepository.findByTypeEquals(type);
        List<InterestDto> interestDTOList = new ArrayList<>();

        for(InterestCategory interestCategory: interestCategories) {
            InterestDto interestDTO = InterestDto.builder()
                    .id(interestCategory.getId())
                    .name(interestCategory.getName())
                    .type(interestCategory.getType())
                    .createdDate(interestCategory.getRegDate())
                    .modifiedDate(interestCategory.getModDate())
                    .on_off(interestCategory.getOn_off())
                    .count(mUserInterestRepository.countByInterest_Id(interestCategory.getId()))
                    .build();

            interestDTOList.add(interestDTO);
        }
        return interestDTOList;
    }

    public InterestDto getInterest(Long id) {
        Optional<InterestCategory> interestWrapper = mInterestRepository.findById(id);
        InterestCategory interestCategory = interestWrapper.get();

        InterestDto interestDTO = InterestDto.builder()
                .id(interestCategory.getId())
                .name(interestCategory.getName())
                .type(interestCategory.getType())
                .build();

        return interestDTO;
    }

    public void deleteInterest(Long id) {
        mInterestRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, String name) throws Exception {
        InterestCategory interest = mInterestRepository.findById(id)
                                                        .orElseThrow(()->new Exception());

        interest.update(name.trim());
    }

    @Transactional
    public void setOnOff(Long id) throws Exception {
        InterestCategory interest = mInterestRepository.findById(id).orElseThrow(()->new Exception());

        if(interest.getOn_off()==0) {
            interest.changeOnOff((byte)1);
        } else {
            interest.changeOnOff((byte)0);
        }
    }

    public List<InterestName> getAllInterest() {
//        List<InterestCategory> all = mInterestRepository.findAll();
        List<InterestCategory> all = mInterestRepository.findAllByOn_offEquals((byte)1);
        List<InterestName> result = new ArrayList<>();

        for(InterestCategory interest : all) {
            InterestName build = InterestName.builder()
                    .id(interest.getId())
                    .name(interest.getName())
                    .build();

            result.add(build);
        }

        return result;
    }
}
