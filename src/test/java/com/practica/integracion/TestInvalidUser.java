package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {
	@Mock
	private AuthDAO authDao;
	@Mock
	private GenericDAO genericDao;
	
	private Object remote;
	private SystemManager systemManager;
	
	@BeforeEach
	void setUp() throws Exception {
		new User("10","nombre","apellido","dir",new ArrayList<Object>());
		remote = new Object();
		
		systemManager = new SystemManager(authDao,genericDao);
	}
	
	@Test
	public void testAddRemoteSystem() throws OperationNotSupportedException, SystemManagerException{		
		when(authDao.getAuthData("10")).thenReturn(null);
		when(genericDao.updateSomeData(null, remote)).thenReturn(false); 
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		
		assertThrows(SystemManagerException.class, () -> {systemManager.addRemoteSystem("10", remote);});
		verify(authDao, times(1)).getAuthData("10");
		verify(genericDao, times(1)).updateSomeData(null,remote);
		
		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).updateSomeData(null, remote);
	}
	
	@Test
	public void testStartRemoteSystem() throws OperationNotSupportedException, SystemManagerException{		
		when(authDao.getAuthData("10")).thenReturn(null);
		when(genericDao.getSomeData(Mockito.any(),Mockito.anyString())).thenThrow(OperationNotSupportedException.class);
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		
		assertThrows(SystemManagerException.class, () -> {systemManager.startRemoteSystem("10", "remId");});
		verify(authDao, times(1)).getAuthData("10");
		verify(genericDao, times(1)).getSomeData(null,"where id=remId");
		
		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).getSomeData(null, "where id=remId");
	}
	
	@Test
	public void testStopRemoteSystem() throws OperationNotSupportedException, SystemManagerException{
		when(authDao.getAuthData("10")).thenReturn(null);
		when(genericDao.getSomeData(Mockito.any(),Mockito.anyString())).thenThrow(OperationNotSupportedException.class);
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		
		assertThrows(SystemManagerException.class, () -> {systemManager.stopRemoteSystem("10", "1");});
		verify(authDao, times(1)).getAuthData("10");
		verify(genericDao, times(1)).getSomeData(null,"where id=1");
		
		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).getSomeData(null, "where id=1");
		
	}
	
	@Test
	public void testDeleteRemoteSystem() throws OperationNotSupportedException, SystemManagerException{
		when(authDao.getAuthData("10")).thenReturn(null);
		when(genericDao.deleteSomeData(Mockito.any(), Mockito.anyString())).thenReturn(false); //quizá la excepción
		InOrder inOrder = Mockito.inOrder(authDao,genericDao);
		
		assertThrows(SystemManagerException.class, () -> {systemManager.deleteRemoteSystem("10", "remId");});
		verify(authDao, times(1)).getAuthData("10");
		verify(genericDao, times(1)).deleteSomeData(null,"remId");

		inOrder.verify(authDao).getAuthData("10");
		inOrder.verify(genericDao).deleteSomeData(null, "remId");
	}
}
