package edu.clarkson.cs.frm.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.kooobao.common.domain.dao.Cursor;
import com.kooobao.common.domain.dao.Dao;
import com.kooobao.common.domain.entity.SimpleEntity;
import com.kooobao.common.json.JsonBeanReader;
import com.kooobao.common.json.JsonWriter;

public class AbstractFileDao<Entity extends SimpleEntity> implements
		Dao<Entity>, InitializingBean {

	private String fileName;

	private long syncInterval = 5000;

	protected Map<Long, Entity> storage;

	protected ReadWriteLock lock = new ReentrantReadWriteLock();

	public AbstractFileDao() {
		super();
		storage = new HashMap<Long, Entity>();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		doLoad();
		// start a thread to do regular sync
		new SyncThread().start();
	}

	protected void doLoad() {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			JsonBeanReader<Entity> reader = new JsonBeanReader<Entity>();
			while ((line = br.readLine()) != null) {
				store(reader.read(line));
			}
			br.close();
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(
					"Failed to load file content", e);
			storage.clear();
		}
	}

	protected void doSync() {
		lock.readLock().lock();
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			JsonWriter writer = new JsonWriter();
			for (Entity e : storage.values()) {
				writer.write(e);
				pw.println(writer.toString());
			}
			pw.close();
		} catch (Exception e) {
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Entity find(long oid) {
		lock.readLock().lock();
		try {
			return storage.get(oid);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Entity store(Entity entity) {
		lock.writeLock().lock();
		try {
			if (entity.getOid() == 0)
				entity.setOid(storage.size());
			storage.put(entity.getOid(), entity);
			return entity;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Entity remove(Entity entity) {
		lock.writeLock().lock();
		try {
			return storage.remove(entity.getOid());
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Entity find(Entity example) {
		lock.readLock().lock();
		try {
			for (Entity val : storage.values()) {
				if (val.equals(example)) {
					return val;
				}
			}
			return null;
		} finally {
			lock.readLock().unlock();
		}
	}

	protected class ListCursor implements Cursor<Entity> {

		public ListCursor(List<Entity> data) {
			this.iterator = data.iterator();
		}

		private Iterator<Entity> iterator;

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public Entity next() {
			return iterator.next();
		}
	}

	@Override
	public Cursor<Entity> findAll() {
		lock.readLock().lock();
		try {
			final List<Entity> list = new ArrayList<Entity>();
			list.addAll(storage.values());
			return new ListCursor(list);
		} finally {
			lock.readLock().unlock();
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	protected class SyncThread extends Thread {

		public SyncThread() {
			super();
			setName(AbstractFileDao.this.getClass().getSimpleName()
					+ "-MonitorThread");
			setDaemon(true);
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(syncInterval);
					doSync();
				} catch (Exception e) {
					LoggerFactory.getLogger(getClass()).error(
							"Exception when doing sync");
				}
			}
		}
	}

	@Override
	public void removeAll() {
		storage.clear();
	}

}
