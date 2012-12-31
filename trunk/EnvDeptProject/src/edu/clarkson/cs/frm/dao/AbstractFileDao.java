package edu.clarkson.cs.frm.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.NoResultException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.google.gson.Gson;
import com.kooobao.common.domain.dao.Cursor;
import com.kooobao.common.domain.dao.Dao;
import com.kooobao.common.domain.entity.SimpleEntity;

public abstract class AbstractFileDao<Entity extends SimpleEntity, Stub extends SimpleEntity>
		implements Dao<Entity>, InitializingBean {

	protected static Map<Class, AbstractFileDao> daos;
	static {
		daos = new HashMap<Class, AbstractFileDao>();
	}

	private String fileName;

	private long syncInterval = 5000;

	private long oidCounter = 1;

	protected Map<Long, Stub> storage;

	protected ReadWriteLock lock = new ReentrantReadWriteLock();

	protected boolean dirty = false;

	public AbstractFileDao() {
		super();
		daos.put(getParamClass(), this);
		storage = new HashMap<Long, Stub>();
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
			Gson gson = new Gson();
			while ((line = br.readLine()) != null) {
				Stub e = gson.fromJson(line, getParamClass2());
				oidCounter = Math.max(oidCounter, e.getOid() + 1);
				if (e.getOid() == 0) {
					e.setOid(oidCounter++);
				}
				storage.put(e.getOid(), e);
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
			if (!dirty)
				return;
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			Gson gson = new Gson();
			for (Stub e : storage.values()) {
				pw.println(gson.toJson(e));
			}
			pw.close();
			dirty = false;
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(
					"Exception in AbstractFileDao.doSync", e);
		} finally {
			lock.readLock().unlock();
		}
	}

	protected abstract Stub wrap(Entity e);

	protected abstract Entity unwrap(Stub o);

	@Override
	public Entity find(long oid) {
		lock.readLock().lock();
		try {
			if (!storage.containsKey(oid))
				throw new NoResultException();
			return unwrap(storage.get(oid));
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Entity store(Entity entity) {
		lock.writeLock().lock();
		try {
			dirty = true;
			if (entity.getOid() == 0)
				entity.setOid(oidCounter++);
			storage.put(entity.getOid(), wrap(entity));
			return entity;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Entity remove(Entity entity) {
		lock.writeLock().lock();
		try {
			dirty = true;
			return unwrap(storage.remove(entity.getOid()));
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Entity find(Entity example) {
		lock.readLock().lock();
		try {
			Stub s = wrap(example);
			for (Stub val : storage.values()) {
				if (val.equals(s)) {
					return unwrap(val);
				}
			}
			throw new NoResultException();
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
			for (Stub s : storage.values())
				list.add(unwrap(s));
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
		lock.readLock().lock();
		try {
			dirty = true;
			storage.clear();
		} finally {
			lock.readLock().unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public Class<Entity> getParamClass() {
		return (Class<Entity>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public Class<Stub> getParamClass2() {
		return (Class<Stub>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}
}
