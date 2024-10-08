package com.example.todoapp.presentation.screen.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSettingsBinding


class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        setupHeader()
        setupSettingItems()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupHeader() {
        binding.btSettingsBack.setOnClickListener{
            findNavController()
                .navigate(R.id.action_settingsFragment_to_homeFragment)
        }
    }

    private fun setupSettingItems() {
        val sections = listOf(
            SettingSections.Account(
                listOf(
                    SettingSectionItem(
                        icon = R.drawable.ic_lock_24,
                        name = "Mudar senha"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_crown_24,
                        name = "Ativar o premium!",
                        color = R.color.yellow_400
                    ),
                )
            ),
            SettingSections.Preferences(
                listOf(
                    SettingSectionItem(
                        icon = R.drawable.ic_theme_brush_24,
                        name = "Tema"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_color_scheme_brush_24,
                        name = "Cores"
                    ),
                )
            ),
            SettingSections.More(
                listOf(
                    SettingSectionItem(
                        icon = R.drawable.ic_messages_24,
                        name = "Feedback"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_star_24,
                        name = "Envie sua avaliação"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_support_24,
                        name = "Suporte"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_message_question_24,
                        name = "Dúvidas"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_shield_tick_24,
                        name = "Políticas de privacidade"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_clipboard_text_24,
                        name = "Termos de uso"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_info_circle_24,
                        name = "Sobre nós"
                    ),
                    SettingSectionItem(
                        icon = R.drawable.ic_logout_24,
                        name = "Sair",
                        color = R.color.red_300
                    ),
                )
            )
        )

        sections.forEach { section ->
            val settingsRv = when(section) {
                is SettingSections.Preferences -> binding.rvSettingsPreference
                is SettingSections.More -> binding.rvSettingsMore
                is SettingSections.Account -> binding.rvSettingsAccount
            }

            settingsRv.layoutManager = LinearLayoutManager(requireContext())
            settingsRv.adapter = SettingListAdapter(section.items)
        }
    }
}