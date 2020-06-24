package com.sys.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sys.bo.Base;
import com.sys.bo.Customer;
import com.sys.bo.LinkWay;
import com.sys.db.DBConnection;
import com.sys.util.PinyinUtil;
import com.sys.util.StringUtil;
import com.sys.vo.CustomerLink;

public class CustomerService {

	private static CustomerService customerService;
	
	private static LinkWayService linkWayService;
	
	private DBConnection connection;
	
	private CustomerService() {
		connection = DBConnection.getInstance();
		linkWayService = LinkWayService.getInstance();
	}
	
	public static synchronized CustomerService getInstance() {
		if (customerService == null) {
			customerService = new CustomerService();
		}
		return customerService;
	}

	public List<Customer> getCustomerByQuery() {
		try {
			String sql = "select * from Customer where isvalid = 1 order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareCustomerList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Customer>();
	}
	
	public List<Customer> getCustomerAll() {
		try {
			String sql = "select * from Customer order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareCustomerList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Customer>();
	}
	
	public List<CustomerLink> getCustomerLinkByKeyword(String keyword) {
		try {
			String sql;
			if (StringUtil.isNotBlank(keyword)) {
				keyword = keyword.toUpperCase();
				sql = "select "+
						  "Customer.id as customerId, "+
						  "Customer.name as customerName, "+
						  "Customer.certNo as customerCertNo, "+
						  "LinkWay.phone as customerPhone "+
					 "from LinkWay, "+
					 	  "Customer "+
					"where LinkWay.isvalid = 1 "+
				      "and Customer.isvalid = 1 "+
				      "and LinkWay.customerId = Customer.id "+
				      "and (spell like '%"+keyword+"%' or name like '%"+keyword+"%') "+
				 "order by Customer.spell asc, LinkWay.createTime desc";
			} else {
				return new ArrayList<CustomerLink>();
			}
			
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareCustomerLink(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return new ArrayList<CustomerLink>();
	}
	
	public CustomerLink prepareCustomerLinkWayWithCertNo(String name, String phone, String certNo) {
		try {
			String sql;
			if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(phone)) {
				sql = "select "+
						  "Customer.id as customerId, "+
						  "Customer.name as customerName, "+
						  "Customer.certNo as customerCertNo, "+
						  "LinkWay.phone as customerPhone "+
					 "from LinkWay, "+
					 	  "Customer "+
					"where LinkWay.isvalid = 1 "+
				      "and Customer.isvalid = 1 "+
				      "and LinkWay.customerId = Customer.id "+
				      "and Customer.name = '"+name+"' "+
				      "and Customer.certNo = '"+certNo+"' "+
				      "and LinkWay.phone = '"+phone+"' ";
				ResultSet resultSet = connection.executeQuery(sql);
				List<CustomerLink> customerLinkList = prepareCustomerLink(resultSet);
				if (customerLinkList.size()>0) {
					return customerLinkList.get(0);
				} else {
					String custSql = "select * from Customer " +
									 "where Customer.isvalid = 1 " +
									 "and Customer.name = '"+name+"'";
					ResultSet custSet = connection.executeQuery(custSql);
					List<Customer> customers = prepareCustomerList(custSet);
					if (customers.size()>0) {
						Customer customer = customers.get(0);
						customer.setCertNo(certNo);
						this.updateCustomer(customer);
						LinkWay linkWay = new LinkWay(customer.getId(), phone);
						linkWayService.saveLinkWay(linkWay);
						return new CustomerLink(customer.getId(), customer.getName(), customer.getCertNo(), linkWay.getPhone());
					} else {
						Customer customer = new Customer(name, certNo);
						customer = this.saveCustomer(customer);
						LinkWay linkWay = new LinkWay(customer.getId(), phone);
						linkWayService.saveLinkWay(linkWay);
						return new CustomerLink(customer.getId(), customer.getName(), customer.getCertNo(), linkWay.getPhone());
					}					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return null;
	}
	
	public CustomerLink prepareCustomerLinkWayInfo(String name, String phone) {
		try {
			String sql;
			if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(phone)) {
				sql = "select "+
						  "Customer.id as customerId, "+
						  "Customer.name as customerName, "+
						  "Customer.certNo as customerCertNo, "+
						  "LinkWay.phone as customerPhone "+
					 "from LinkWay, "+
					 	  "Customer "+
					"where LinkWay.isvalid = 1 "+
				      "and Customer.isvalid = 1 "+
				      "and LinkWay.customerId = Customer.id "+
				      "and Customer.name = '"+name+"' "+
				      "and LinkWay.phone = '"+phone+"' ";
				ResultSet resultSet = connection.executeQuery(sql);
				List<CustomerLink> customerLinkList = prepareCustomerLink(resultSet);
				if (customerLinkList.size()>0) {
					return customerLinkList.get(0);
				} else {
					String custSql = "select * from Customer " +
									 "where Customer.isvalid = 1 " +
									 "and Customer.name = '"+name+"'";
					ResultSet custSet = connection.executeQuery(custSql);
					List<Customer> customers = prepareCustomerList(custSet);
					if (customers.size()>0) {
						Customer customer = customers.get(0);
						LinkWay linkWay = new LinkWay(customer.getId(), phone);
						linkWayService.saveLinkWay(linkWay);
						return new CustomerLink(customer.getId(), customer.getName(), customer.getCertNo(), linkWay.getPhone());
					} else {
						Customer customer = new Customer(name);
						customer = this.saveCustomer(customer);
						LinkWay linkWay = new LinkWay(customer.getId(), phone);
						linkWayService.saveLinkWay(linkWay);
						return new CustomerLink(customer.getId(), customer.getName(), customer.getCertNo(), linkWay.getPhone());
					}					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return null;
	}
	
	public List<Customer> getCustomerByKeyword(String keyword) {					
		try {
			String sql;
			if (StringUtil.isNotBlank(keyword)) {
				keyword = keyword.toUpperCase();
				sql = "select * from Customer where isvalid = 1 and (spell like '%"+keyword+"%' or name like '%"+keyword+"%') order by createTime desc";
			} else {
				sql = "select * from Customer where isvalid = 1 order by createTime desc";
			}
			
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareCustomerList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return new ArrayList<Customer>();
	}
	
	public List<Customer> getCustomerByName(String name) {
		try {		
			String sql = "select * from Customer where isvalid = 1 and name = '"+name+"' order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareCustomerList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Customer>();
	}
	
	public Customer getCustomerById(String id) {
		try {		
			String sql = "select * from Customer where id = '"+id+"'";
			ResultSet resultSet = connection.executeQuery(sql);
			List<Customer> customers = prepareCustomerList(resultSet);
			if (customers!=null && customers.size()>0) {
				return customers.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Customer saveCustomer(String name) {
		if (StringUtil.isNotBlank(name)) {
			List<Customer> customers = this.getCustomerByName(name);
			if (customers==null || customers.size()==0) {
				Customer customer = new Customer();
				customer.setName(name);
				customer = this.saveCustomer(customer);
				return customer;
			} else {
				return customers.get(0);
			}
		}
		return null;
	}
	
	public Customer saveCustomer(Customer customer) {
		if (customer!=null) {
			customer.setId(Base.generateUUID());
			customer.setSpell(PinyinUtil.getFirstSpell(customer.getName()));
			long currentTime = System.currentTimeMillis();
			String sql = String.format("insert into customer (" +
					"id, " +
					"name, " +
					"certNo, " +
					"spell, " +
					"isvalid, " +
					"createTime, " +
					"opTime) values ('%s', '%s', '%s', '%s', '1', '%d', '%d')",
					customer.getId(),
					customer.getName(),
					customer.getCertNo(),
					customer.getSpell(),
					currentTime,
					currentTime);					
			connection.executeUpdate(sql);			
		}
		return customer;
	}
	
	public void updateCustomer(Customer customer) {
		if (customer!=null) {
			customer.setSpell(PinyinUtil.getFirstSpell(customer.getName()));
			long currentTime = System.currentTimeMillis();
			String sql = String.format("update customer set " +
					"name = '%s'," +
					"certNo = '%s'," +
					"spell = '%s'," +
					"opTime = '"+currentTime+"' where id = '%s'",
					customer.getName(),
					customer.getCertNo(),
					customer.getSpell(),
					customer.getId());
			connection.executeUpdate(sql);
		}
	}
	
	public void removeCustomer(Customer customer) {
		if (customer!=null) {
			String sql = String.format("update customer set isvalid = '0' where id = '%s'", customer.getId());
			connection.executeUpdate(sql);
		}
	}
	
	private List<Customer> prepareCustomerList(ResultSet resultSet) {
		if (resultSet!=null) {
			try{
				List<Customer> customerList = new ArrayList<Customer>();
				while(resultSet.next()) {
					Customer customer = new Customer();
					customer.setId(resultSet.getString("id"));
					customer.setName(resultSet.getString("name"));
					customer.setSpell(resultSet.getString("spell"));
					customer.setCertNo(resultSet.getString("certNo"));
					customer.setIsValid(resultSet.getShort("isValid"));
					customer.setCreateTime(resultSet.getLong("createTime"));
					customer.setOpTime(resultSet.getLong("opTime"));			
					customerList.add(customer);
				}
				return customerList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<Customer>();
	}
	
	private List<CustomerLink> prepareCustomerLink(ResultSet resultSet) {
		if (resultSet!=null) {
			try{
				List<CustomerLink> customerLinkList = new ArrayList<CustomerLink>();
				while(resultSet.next()) {
					CustomerLink customerLink = new CustomerLink();
					customerLink.setCustomerId(resultSet.getString("customerId"));
					customerLink.setCustomerName(resultSet.getString("customerName"));
					customerLink.setCustomerCertNo(resultSet.getString("customerCertNo"));
					customerLink.setCustomerPhone(resultSet.getString("customerPhone"));		
					customerLinkList.add(customerLink);
				}
				return customerLinkList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<CustomerLink>();
	}
}
