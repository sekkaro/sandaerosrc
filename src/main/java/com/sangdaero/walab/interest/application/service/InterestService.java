package com.sangdaero.walab.interest.application.service;


import com.sangdaero.walab.common.entity.InterestCategory;

import com.sangdaero.walab.interest.application.dto.InterestDto;
import com.sangdaero.walab.interest.application.dto.InterestName;
import com.sangdaero.walab.interest.domain.repository.InterestRepository;
import com.sangdaero.walab.mapper.repository.UserInterestRepository;
import org.springframework.stereotype.Service;

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
        return mInterestRepository.save(interestDto.toEntity()).getId();
    }

    public List<InterestDto> getInterestList() {
//        List<InterestName> interestNames = mInterestRepository.findAllByOrderByName();
        List<InterestCategory> interestCategories = mInterestRepository.findAll();
//        List<InterestCategory> interestCategories = mInterestRepository.findByTypeEquals(type);
        List<InterestDto> interestDTOList = new ArrayList<>();

        for(InterestCategory interestCategory: interestCategories) {
            InterestDto interestDTO = InterestDto.builder()
                    .id(interestCategory.getId())
                    .name(interestCategory.getName())
                    .type(interestCategory.getType())
                    .on_off(interestCategory.getOn_off())
                    .createdDate(interestCategory.getRegDate())
                    .modifiedDate(interestCategory.getModDate())
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
                .on_off(interestCategory.getOn_off())
                .build();

        return interestDTO;
    }

    public void deleteInterest(Long id) {
        mInterestRepository.deleteById(id);
    }
}
