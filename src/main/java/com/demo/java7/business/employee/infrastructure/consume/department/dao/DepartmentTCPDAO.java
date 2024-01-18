package com.demo.java7.business.employee.infrastructure.consume.department.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Vector;

import com.demo.java7.business.employee.infrastructure.consume.department.mapper.DepartmentMapper;
import com.demo.java7.business.department.domain.model.DepartmentDto;
import com.demo.java7.business.infrastructure.constant.NetworkConstant;
import com.demo.java7.business.infrastructure.enums.TcpServicesEnum;

/**
 * Tendrá tantos métodos estáticos como servicios ofrece el servidor TCP.
 */
public class DepartmentTCPDAO {

  public static Collection<DepartmentDto> findAll() {
    Socket socket = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream = null;

    try {
      socket = new Socket(NetworkConstant.SERVER_IP, NetworkConstant.DEPARTMENTS_TCP_SERVICE_PORT);
      outputStream = new DataOutputStream(socket.getOutputStream());
      inputStream = new DataInputStream(socket.getInputStream());

      outputStream.writeInt(TcpServicesEnum.DEPARTMENTS_FIND_ALL.getServiceCode()); //envío al servidor el código correspondiente al servicio
      int departmentsNumber = inputStream.readInt(); //recupero del servidor el tamaño de la lista que enviará

      Vector<DepartmentDto> departmentList = new Vector<>();
      String serverResponse;

      for (int i = 0; i < departmentsNumber; i++) {
        serverResponse = inputStream.readUTF(); // recupero cada objeto en formato toString
        departmentList.add(DepartmentMapper.stringToDepartment(serverResponse));
      }
      return departmentList;

    } catch (Exception exception) {
      throw new RuntimeException("error to receive department list: " + exception.getMessage());

    } finally {
      try {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
        if (socket != null) socket.close();

      } catch (Exception exception) {
        throw new RuntimeException("error to close TCP connection: " + exception.getMessage());
      }
    }
  }

  public static DepartmentDto findByCode(int departmentCode) {
    Socket socket = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream = null;

    try {
      socket = new Socket(NetworkConstant.SERVER_IP, NetworkConstant.DEPARTMENTS_TCP_SERVICE_PORT);
      outputStream = new DataOutputStream(socket.getOutputStream());
      inputStream = new DataInputStream(socket.getInputStream());

      outputStream.writeInt(TcpServicesEnum.DEPARTMENTS_FIND_BY_CODE.getServiceCode());
      outputStream.writeUTF(Integer.toString(departmentCode)); //envio el department code

      String serverResponse = inputStream.readUTF();
      return DepartmentMapper.stringToDepartment(serverResponse);

    } catch (Exception exception) {
      throw new RuntimeException("error to receive department by code: " + exception.getMessage());

    } finally {
      try {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
        if (socket != null) socket.close();

      } catch (Exception exception) {
        throw new RuntimeException("error to close TCP connection: " + exception.getMessage());
      }
    }
  }

}