package io.bakerystud.smartpit

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import toothpick.Toothpick
import java.util.*


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.setVisibility(visible: Boolean) {
    this.visibility = if (!visible) View.GONE else View.VISIBLE
}


inline fun <reified T : ViewModel> FragmentActivity.getViewModel(
    scope: String,
    providerClass: Class<*>
): T {
    val provider = Toothpick.openScope(scope).getInstance(providerClass) as ViewModelProvider.Factory
    val modelProvider = ViewModelProvider(this, provider)
    return modelProvider[T::class.java]
}

fun Activity.showSnackbar(message: String?, short: Boolean = true) {
    message ?: return
    val duration = if (short) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_LONG
    Snackbar.make(this.findViewById(android.R.id.content), message, duration).show()
}

fun Activity.showSnackbar(messageRes: Int?, short: Boolean = true) {
    messageRes ?: return
    showSnackbar(getString(messageRes), short)
}

fun Fragment.showSnackbar(message: String?, short: Boolean = true) {
    activity?.showSnackbar(message, short)
}

fun Fragment.showSnackbar(message: Int?, short: Boolean = true) {
    activity?.showSnackbar(message, short)
}

fun Activity.hideKeyboard() {
    val keyboardManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    keyboardManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val keyboardManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    keyboardManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun FragmentActivity.currentFragment(): Fragment? {
    val fragmentManager = this.supportFragmentManager
    val fragments = fragmentManager.fragments
    for (fragment in fragments) {
        if (fragment != null && fragment.isVisible)
            return fragment
    }
    return null
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT)
            .show()
}

fun Context.toast(stringId: Int) {
    toast(getString(stringId))
}

fun Fragment.toast(text: String) {
    activity?.toast(text)
}

fun Fragment.toast(stringId: Int) {
    context?.toast(stringId)
}

fun Fragment.dimen(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}

fun Activity.dimen(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}

fun View.dimen(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}

fun Context.dimen(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}

fun Activity.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Fragment.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(requireContext(), id)
}

fun View.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this.context, id)
}

fun String.toEditable(): Editable {
    return SpannableStringBuilder(this)
}

val Calendar.year: Int
    get() = get(Calendar.YEAR)
val Calendar.month: Int
    get() = this.get(Calendar.MONTH)
val Calendar.day: Int
    get() = get(Calendar.DAY_OF_MONTH)
val Calendar.hour: Int
    get() = get(Calendar.HOUR_OF_DAY)
val Calendar.minute: Int
    get() = get(Calendar.MINUTE)
