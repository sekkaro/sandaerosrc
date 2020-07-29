package com.sangdaero.walab.common.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sangdaero.walab.common.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

}
