package br.com.livroandroid.carros.adapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.fragments.CarrosFragment
import br.com.livroandroid.carros.fragments.FavoritosFragment

class TabsAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    // Qtde de Tabs
    override fun getCount() = 4

    // Retorna o tipo pela posição
    private fun getTipoCarro(position: Int) = when(position) {
        0 -> TipoCarro.Classicos
        1 -> TipoCarro.Esportivos
        2 -> TipoCarro.Luxo
        else -> TipoCarro.Favoritos
    }

    // Título da Tab
    override fun getPageTitle(position: Int): CharSequence {
        val tipo = getTipoCarro(position)
        return context.getString(tipo.string)
    }

    // Fragment que vai mostrar a lista de carros
    override fun getItem(position: Int): Fragment {
        if(position == 3) {
            return FavoritosFragment()
        }

        val tipo = getTipoCarro(position)
        val f: Fragment = CarrosFragment()
        f.arguments  = Bundle()
        f.arguments?.putSerializable("tipo", tipo)
        return f
    }
}