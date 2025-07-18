import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _catId = MutableLiveData<Int>().apply { value = 0 }
    val catId: LiveData<Int> get() = _catId

    private val _subCatId = MutableLiveData<Int>().apply { value = 0 }
    val subCatId: LiveData<Int> get() = _subCatId

    fun setCatId(id: Int) {
        _catId.value = id
    }

    fun setSubCatId(id: Int) {
        _subCatId.value = id
    }
}