package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppSettings
import com.example.data.model.AuditLog
import com.example.data.model.Cow
import com.example.data.model.CrackerOrder
import com.example.data.model.CrackerProduct
import com.example.data.model.Donation
import com.example.data.model.Expense
import com.example.data.model.Festival
import com.example.data.model.GalleryItem
import com.example.data.model.GaushalaEvent
import com.example.data.model.GaushalaService
import com.example.data.model.PamphletCategory
import com.example.data.model.PamphletTheme
import com.example.data.model.SevaItem
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface GaushalaDao {

  // Users
  @Query("SELECT * FROM users ORDER BY registeredAt DESC")
  fun getAllUsers(): Flow<List<User>>

  @Query("SELECT * FROM users WHERE mobileNumber = :mobile LIMIT 1")
  suspend fun getUserByMobile(mobile: String): User?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: User): Long

  @Update
  suspend fun updateUser(user: User)

  @Query("DELETE FROM users WHERE id = :id")
  suspend fun deleteUserById(id: Long)

  // Donations
  @Query("SELECT * FROM donations ORDER BY createdAt DESC")
  fun getAllDonations(): Flow<List<Donation>>

  @Query("SELECT * FROM donations WHERE paymentStatus = 'SUCCESS' ORDER BY createdAt DESC")
  fun getSuccessfulDonations(): Flow<List<Donation>>

  @Query("SELECT * FROM donations WHERE mobile = :mobile ORDER BY createdAt DESC")
  fun getDonationsByMobile(mobile: String): Flow<List<Donation>>

  @Query("SELECT * FROM donations WHERE id = :id LIMIT 1")
  suspend fun getDonationById(id: String): Donation?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDonation(donation: Donation)

  @Query("DELETE FROM donations WHERE id = :id")
  suspend fun deleteDonationById(id: String)

  // Festivals
  @Query("SELECT * FROM festivals ORDER BY sortOrder ASC, id DESC")
  fun getAllFestivals(): Flow<List<Festival>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFestival(festival: Festival): Long

  @Update
  suspend fun updateFestival(festival: Festival)

  @Query("DELETE FROM festivals WHERE id = :id")
  suspend fun deleteFestivalById(id: Long)

  // Seva Items
  @Query("SELECT * FROM seva_items ORDER BY sortOrder ASC, id ASC")
  fun getAllSevaItems(): Flow<List<SevaItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSevaItem(item: SevaItem): Long

  @Update
  suspend fun updateSevaItem(item: SevaItem)

  @Query("DELETE FROM seva_items WHERE id = :id")
  suspend fun deleteSevaItemById(id: Long)

  // Cows
  @Query("SELECT * FROM cows ORDER BY id ASC")
  fun getAllCows(): Flow<List<Cow>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCow(cow: Cow): Long

  @Update
  suspend fun updateCow(cow: Cow)

  @Query("DELETE FROM cows WHERE id = :id")
  suspend fun deleteCowById(id: Long)

  // Crackers
  @Query("SELECT * FROM cracker_products ORDER BY sortOrder ASC, id ASC")
  fun getAllCrackerProducts(): Flow<List<CrackerProduct>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCrackerProduct(product: CrackerProduct): Long

  @Update
  suspend fun updateCrackerProduct(product: CrackerProduct)

  @Query("DELETE FROM cracker_products WHERE id = :id")
  suspend fun deleteCrackerProductById(id: Long)

  // Cracker Orders
  @Query("SELECT * FROM cracker_orders ORDER BY createdAt DESC")
  fun getAllCrackerOrders(): Flow<List<CrackerOrder>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCrackerOrder(order: CrackerOrder)

  @Update
  suspend fun updateCrackerOrder(order: CrackerOrder)

  // Expenses
  @Query("SELECT * FROM expenses ORDER BY createdAt DESC")
  fun getAllExpenses(): Flow<List<Expense>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertExpense(expense: Expense): Long

  @Query("DELETE FROM expenses WHERE id = :id")
  suspend fun deleteExpenseById(id: Long)

  // Services
  @Query("SELECT * FROM gaushala_services ORDER BY id ASC")
  fun getAllServices(): Flow<List<GaushalaService>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertService(service: GaushalaService): Long

  @Update
  suspend fun updateService(service: GaushalaService)

  @Query("DELETE FROM gaushala_services WHERE id = :id")
  suspend fun deleteServiceById(id: Long)

  // Events
  @Query("SELECT * FROM events ORDER BY id DESC")
  fun getAllEvents(): Flow<List<GaushalaEvent>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEvent(event: GaushalaEvent): Long

  @Update
  suspend fun updateEvent(event: GaushalaEvent)

  @Query("DELETE FROM events WHERE id = :id")
  suspend fun deleteEventById(id: Long)

  // Gallery
  @Query("SELECT * FROM gallery_items ORDER BY sortOrder ASC, id ASC")
  fun getAllGalleryItems(): Flow<List<GalleryItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGalleryItem(item: GalleryItem): Long

  @Query("DELETE FROM gallery_items WHERE id = :id")
  suspend fun deleteGalleryItemById(id: Long)

  // Pamphlet Categories
  @Query("SELECT * FROM pamphlet_categories ORDER BY sortOrder ASC, id ASC")
  fun getAllPamphletCategories(): Flow<List<PamphletCategory>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPamphletCategory(category: PamphletCategory): Long

  @Update
  suspend fun updatePamphletCategory(category: PamphletCategory)

  @Query("DELETE FROM pamphlet_categories WHERE id = :id")
  suspend fun deletePamphletCategoryById(id: Long)

  @Query("UPDATE pamphlet_categories SET selectionMode = :mode WHERE `key` = :key")
  suspend fun updateCategorySelectionMode(key: String, mode: String)

  // Pamphlet Themes
  @Query("SELECT * FROM pamphlet_themes ORDER BY sortOrder ASC, id ASC")
  fun getAllPamphletThemes(): Flow<List<PamphletTheme>>

  @Query("SELECT * FROM pamphlet_themes WHERE id = :id LIMIT 1")
  fun getPamphletThemeById(id: Long): Flow<PamphletTheme?>

  @Query("SELECT * FROM pamphlet_themes WHERE category = :category AND isActive = 1 ORDER BY sortOrder ASC, id ASC")
  suspend fun getActiveThemesForCategory(category: String): List<PamphletTheme>

  @Query("SELECT * FROM pamphlet_themes WHERE category = :category AND subCategory = :subCategory AND isActive = 1 ORDER BY sortOrder ASC, id ASC")
  suspend fun getActiveThemesForCategoryAndSubCategory(category: String, subCategory: String): List<PamphletTheme>

  @Query("SELECT * FROM pamphlet_themes WHERE category = :category AND isDefault = 1 AND isActive = 1 LIMIT 1")
  suspend fun getDefaultThemeForCategory(category: String): PamphletTheme?

  @Query("SELECT * FROM pamphlet_themes WHERE category = :category AND subCategory = :subCategory AND isDefault = 1 AND isActive = 1 LIMIT 1")
  suspend fun getDefaultThemeForCategoryAndSubCategory(category: String, subCategory: String): PamphletTheme?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPamphletTheme(theme: PamphletTheme): Long

  @Update
  suspend fun updatePamphletTheme(theme: PamphletTheme)

  @Query("DELETE FROM pamphlet_themes WHERE id = :id")
  suspend fun deletePamphletThemeById(id: Long)

  @Query("UPDATE pamphlet_themes SET isDefault = 0 WHERE category = :category")
  suspend fun clearDefaultThemeForCategory(category: String)

  @Query("UPDATE pamphlet_themes SET isDefault = 0 WHERE category = :category AND subCategory = :subCategory")
  suspend fun clearDefaultThemeForCategoryAndSubCategory(category: String, subCategory: String)

  @Query("UPDATE pamphlet_themes SET isDefault = 1, isActive = 1 WHERE id = :themeId")
  suspend fun markThemeAsDefault(themeId: Long)

  @Query("UPDATE donations SET themeIdUsed = :newThemeId, themeSnapshotJson = :themeSnapshotJson")
  suspend fun applyThemeToAllExistingDonations(newThemeId: Long, themeSnapshotJson: String)

  // Settings
  @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
  fun getAppSettings(): Flow<AppSettings?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveAppSettings(settings: AppSettings)

  // Audit Logs
  @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
  fun getAllAuditLogs(): Flow<List<AuditLog>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAuditLog(log: AuditLog)
}
