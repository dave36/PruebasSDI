package sdi.gtd.persistence;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdi.gtd.util.Builder;
import uo.sdi.dto.Category;
import uo.sdi.dto.User;
import uo.sdi.persistence.CategoryDao;
import uo.sdi.persistence.PersistenceException;
import uo.sdi.persistence.Persistence;
import uo.sdi.persistence.UserDao;

public class CategoryDaoTests {

	private User userDto;
	private UserDao uDao;
	private Long userId;
	private Category catDto;
	private Long catId;
	private CategoryDao cDao;

	@Before
	public void setUp() throws Exception {
		uDao = Persistence.getUserDao();
		cDao = Persistence.getCategoryDao();

		userDto = Builder.newUser();
		userId = uDao.save( userDto );
		userDto.setId( userId );
		
		catDto = Builder.newCategoryFor( userId );
		catId = cDao.save( catDto );
		catDto.setId( catId );
	}

	@After
	public void tearDown() throws Exception {
		cDao.delete( catId );
		uDao.delete( userId );
	}

	/**
	 * Another category with a repeated name cannot be inserted
	 */
	@Test(expected = PersistenceException.class )
	public void testRepeatedSave() {
		String repeatedName = catDto.getName();
		
		Category other = new Category()
				.setName( repeatedName )
				.setUserId(userId);
		
		cDao.save( other );
	}

	/**
	 * A Category with null name cannot be inserted
	 */
	@Test(expected = PersistenceException.class )
	public void testNullNameSave() {
		Category other = new Category()
				// null name
				.setUserId(userId);
		
		cDao.save( other );
	}

	/**
	 * A Category without userId cannot be inserted
	 */
	@Test(expected = PersistenceException.class )
	public void testNullUserIdSave() {
		Category other = new Category()
				.setName("TestCategory (2)")
				.setUserId( null );
		
		cDao.save( other );
	}

	/**
	 * The test Category can be found by its id
	 */
	@Test
	public void testFindById() {
		Category other = cDao.findById( catId );
		assertTrue( other.equals( catDto ) );
	}

	/**
	 * The test Category is among all the rest of categories
	 */
	@Test
	public void testFindAll() {
		List<Category> cats = cDao.findAll();
		assertTrue( cats.contains( catDto ) );
	}

	/**
	 * The name of a Category can be updated
	 */
	@Test
	public void testUpdate() {
		catDto.setName("TestCategory - updated");

		int nr = cDao.update( catDto );
		assertTrue( nr == 1 );
		
		Category updated = cDao.findById( catDto.getId() );
		assertTrue( updated.equals( catDto ) );
	}

	/**
	 * The name of a Category cannot be updated if the new name is repeated
	 */
	@Test(expected = PersistenceException.class)
	public void testUpdateNonUniqueName() {
		Category other = new Category()
				.setName("TestCategory - other")
				.setUserId(userId);
		Long otherId = cDao.save( other );
		
		try {
			catDto.setName("TestCategory - other");  // Repeated name
			cDao.update( catDto );
		}
		finally {
			cDao.delete( otherId );
		}
	}

	/**
	 * dao.delete removes the Category 
	 */
	@Test
	public void testRemove() {
		int nr = cDao.delete( catId );
		assertTrue( nr == 1 );
		
		Category updated = cDao.findById( catDto.getId() );
		assertTrue( updated == null );
	}

	/**
	 * Find all categories of a User, with one
	 */
	@Test
	public void testFindAllForUserWithOne() {
		List<Category> cats = cDao.findByUserId( userId );
		assertTrue( cats.size() == 1 );
		assertTrue( cats.contains( catDto ) );
	}

	/**
	 * Find all categories of a User, with more than one
	 */
	@Test
	public void testFindAllForUserWithSeveral() {
		Category catDto1 = buildAndSaveCategoryForUser(userId);
		Category catDto2 = buildAndSaveCategoryForUser(userId);

		try {
			List<Category> cats = cDao.findByUserId(userId);

			assertTrue(cats.size() == 3);
			assertTrue(cats.contains(catDto));
			assertTrue(cats.contains(catDto1));
			assertTrue(cats.contains(catDto2));
		} finally {
			cDao.delete(catDto1.getId());
			cDao.delete(catDto2.getId());
		}
	}

	private Category buildAndSaveCategoryForUser(Long userId) {
		Category dto = Builder.newCategoryFor(userId);
		Long id = cDao.save(dto);
		dto.setId(id);
		return dto;
	}

	/**
	 * Find all categories of a User, without categories
	 */
	@Test
	public void testFindAllForUserWithNone() {
		cDao.deleteAllFromUserId(userId);

		List<Category> cats = cDao.findByUserId( userId );
		assertTrue( cats.size() == 0 );
	}

	/**
	 * Delete all from user id
	 */
	@Test
	public void testDeleteFromUserId() {
		Category catDto1 = buildAndSaveCategoryForUser(userId);
		Category catDto2 = buildAndSaveCategoryForUser(userId);

		try {
			int nr = cDao.deleteAllFromUserId(userId);
			assertTrue( nr == 3 );

			List<Category> cats = cDao.findByUserId(userId);
			assertTrue(cats.size() == 0);
		} finally {
			cDao.delete(catDto1.getId());
			cDao.delete(catDto2.getId());
		}		
	}
}
