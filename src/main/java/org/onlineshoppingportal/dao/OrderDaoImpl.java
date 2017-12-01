package org.onlineshoppingportal.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.onlineshoppingportal.entity.Order;
import org.onlineshoppingportal.entity.Account;
import org.onlineshoppingportal.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class OrderDaoImpl implements OrderDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private AccountDao accountDao;
	
	private int getMaxOrderNum() {
		String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		Integer value = (Integer) query.uniqueResult();
		if(value == null) {
			return 0;
		}
		return value;
	}
	
	@Override
	public Order saveOrder(String code, int quantity, String username) {
		Session session = sessionFactory.getCurrentSession();
		int orderNum = this.getMaxOrderNum() + 1;
		Account account = accountDao.findAccount(username);
		Product product = productDao.findProduct(code);
		product.setQuantity(product.getQuantity() - quantity);
		Order order = new Order();
		order.setOrderNum(orderNum);
		order.setOrderDate(new Date());
		order.setAmount(quantity * product.getPrice());
		order.setQuantity(quantity);
		order.setAccount(account);
		order.setProduct(product);
		session.save(order);
		return order;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Order> getOrderByUserName(String username) {
		Session session = sessionFactory.getCurrentSession();
		Account account = accountDao.findAccount(username);
		Criteria crit = session.createCriteria(Order.class);
		crit.add(Restrictions.eq("account.userId", account.getUserId()));
		List<Order> list = (List<Order>) crit.list();
		return list;
	}
}
