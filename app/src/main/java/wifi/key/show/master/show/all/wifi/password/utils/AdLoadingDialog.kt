package wifi.key.show.master.show.all.wifi.password.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import wifi.key.show.master.show.all.wifi.password.databinding.FullScreenLoadingDialogBinding

class AdLoadingDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    lateinit var binding: FullScreenLoadingDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        binding = FullScreenLoadingDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}