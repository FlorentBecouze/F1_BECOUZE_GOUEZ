package application.boardingdraft.Frontend.Views

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.boardingdraft.Frontend.Model.Jeu
import application.boardingdraft.Frontend.ViewModel.JeuViewModel
import application.boardingdraft.Frontend.Views.List.ListAdapterJeu
import application.boardingdraft.R

class JeuxFragment : Fragment(R.layout.fragment_jeux) {

    // Accès aux méthodes de récupération / modifications de données de la BDD.
    val jeuViewModel:JeuViewModel by viewModels()
    var recyclerView:RecyclerView? = null
    var listeJeux: MutableList<Jeu> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListAdapterJeu(emptyList())

        // Lien entre la recyclerView affichée et son adaptateur
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_liste_jeux)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter

        // Si on clique sur une cellule de la recyclerView
        adapter.setOnButtonJeuClickedListener(object :ListAdapterJeu.IJeu {
            override fun onButtonJeuClickedListener(position: Int) {
                // Récupération du jeu sélectionné
                val jeuSel: Jeu = (recyclerView?.adapter as ListAdapterJeu).listeJeux[position]

                // Affichage du fragment des "infos d'un jeu", en passant au fragment les données à afficher
                findNavController().navigate(JeuxFragmentDirections.actionJeuxFragmentToJeuxInfosFragment(jeuSel.NomJeu, jeuSel.DescriptionJeu, jeuSel.Photo))
            }
        })

        // Observation en directe de la liste des jeux
        jeuViewModel.currentListeJeux.observe(viewLifecycleOwner) {
                jeux:List<Jeu> ->
            // Si la liste est vide alors on crée les jeux
            // C'est possible lors de la première ouverture de l'application
            if (jeux.isEmpty()) {
                creerJeux()
            }
            (recyclerView?.adapter as ListAdapterJeu).listeJeux = jeux
            recyclerView?.adapter!!.notifyDataSetChanged()
        }
    }

    // Méthode permettant de créer un panel de jeux et de les ajouter dans la BDD
    private fun creerJeux() {
        val image1: ImageView = ImageView(context)
        val image2: ImageView = ImageView(context)
        val image3: ImageView = ImageView(context)
        val image4: ImageView = ImageView(context)
        val image5: ImageView = ImageView(context)
        val image6: ImageView = ImageView(context)
        val image7: ImageView = ImageView(context)

        image1.setImageResource(R.drawable.image_uno)
        image2.setImageResource(R.drawable.image_le_president)
        image3.setImageResource(R.drawable.image_la_bonne_paye)
        image4.setImageResource(R.drawable.image_les_petits_chevaux)
        image5.setImageResource(R.drawable.image_mikado)
        image6.setImageResource(R.drawable.image_le_jeu_de_loie)
        image7.setImageResource(R.drawable.image_phase_10)

        listeJeux.add(Jeu(NomJeu = getString(R.string.titre_jeu_UNO), DescriptionJeu = getString(R.string.desc_jeu_UNO), Photo = genererBitMap(image1)))
        listeJeux.add(Jeu(NomJeu = getString(R.string.titre_jeu_president), DescriptionJeu = getString(R.string.desc_jeu_president), Photo = genererBitMap(image2)))
        listeJeux.add(Jeu(NomJeu = getString(R.string.titre_jeu_bonne_paye), DescriptionJeu = getString(R.string.desc_jeu_bonne_paye), Photo = genererBitMap(image3)))
        listeJeux.add(Jeu(NomJeu = getString(R.string.titre_jeu_petits_chevaux), DescriptionJeu = getString(R.string.desc_jeu_petits_chevaux), Photo = genererBitMap(image4)))
        listeJeux.add(Jeu(NomJeu = getString(R.string.titre_jeu_mikado), DescriptionJeu = getString(R.string.desc_jeu_mikado), Photo = genererBitMap(image5)))
        listeJeux.add(Jeu(NomJeu = getString(R.string.titre_jeu_oie), DescriptionJeu = getString(R.string.desc_jeu_oie), Photo = genererBitMap(image6)))
        listeJeux.add(Jeu(NomJeu = getString(R.string.titre_jeu_phase_10), DescriptionJeu = getString(R.string.desc_jeu_phase_10), Photo = genererBitMap(image7)))

        // Ajout des jeux dans la BDD
        listeJeux.forEach { it ->
            jeuViewModel.insererJeu(it)
        }
    }

    // Méthode servant à transformer une image en Bitmap
    private fun genererBitMap(image: ImageView) : Bitmap {
        val drawable = image.drawable as BitmapDrawable
        return drawable.bitmap
    }
}