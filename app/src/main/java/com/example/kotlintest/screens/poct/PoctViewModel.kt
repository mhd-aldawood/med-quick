package com.example.kotlintest.screens.poct

import androidx.lifecycle.viewModelScope
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.devicesWorker.PoctWorker
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.screens.poct.models.Category
import com.example.kotlintest.screens.poct.models.PoctCardItem
import com.example.kotlintest.screens.poct.models.PoctCardTestHeader
import com.example.kotlintest.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//FIA Testing System
class PoctViewModel @Inject constructor(
    private val worker: PoctWorker
) :
    BaseViewModel<PoctState, PoctEvents, PoctAction>(
        initialState = PoctState()
    ) {
    private val TAG = "PoctViewModel"

    init {
        Logger.i(TAG, "BloodAnalyzerViewModel init")
        viewModelScope.launch {
            worker.startHl7Server(21000) { result ->
            }

        }
    }
    override fun handleAction(action: PoctAction) {
        TODO("Not yet implemented")
    }

}

data class PoctState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "FIA Testing System (POCT)",
        titleIcon = R.drawable.ic_bluetooth_on
    ),
    val poctCardItems: List<PoctCardItem> = mutableListOf<PoctCardItem>(
        PoctCardItem(
            poctCardTestHeader = PoctCardTestHeader(
                headerTitle = "Test1",
                headerDate = "2024/03026 14:07:13"
            ),
            cardFirstColumn = listOf<String>("Item", "Result", "Ref-Range"),
            cardSecondColumn = listOf<Any>("HbA1c", Pair("6.15% High", false), "4-6%"),
            cardThirdColumn = listOf<String>("HbA1c", "43.73 mmol/mol High", "20.22.42.08...mol"),
            cardFourthColumn = listOf<String>("HbA1c", "129.83 mg/dl High", "68.1-125.5mg/dl")
        ),
        PoctCardItem(
            poctCardTestHeader = PoctCardTestHeader(
                headerTitle = "Test1",
                headerDate = "2024/03026 14:07:13"
            ),
            cardFirstColumn = listOf<String>("Item", "Result", "Ref-Range"),
            cardSecondColumn = listOf<Any>("HbA1c", Pair("6.15% High", true), "6-15% High"),
            cardThirdColumn = listOf<String>("HbA1c", "43.73 mmol/mol High", "20.22.42.08...mol"),
            cardFourthColumn = listOf<String>("HbA1c", "129.83 mg/dl High", "68.1-125.5mg/dl")
        ),
    ),
    val list: List<Category> = listOf<Category>(
        Category(
            title = "Infectious Diseases", items = listOf<String>(
                "Flu A+B Ag",
                "SARS-COV-2 Ag",
                "SARS-COV-2/Flu A/Flu B",
                "Anti-SARS-COV-2 S",
                "Dengue IgM/IgG",
                "H. pylori Ag",
                "Dengue NS1 Ag",
                "HBsAg",
                "Strep A",
                "RSV",
                "Rotavirus",
                "Adeno",
                "Norovirus"
            )
        ),
        Category(
            title = "Drug Of Abuse",
            items = listOf<String>(
                "MOP",
                "MET",
                "KET",
                "MOP/MET/KET",
            )
        ),
        Category(
            title = "Renal Function",
            items = listOf<String>(
                "Cys C",
                "MAU",
                "B2-MG",
                "MOP/MET/KET",
            )
        ),
        //first column
        Category(
            title = "Others",
            items = listOf<String>(
                "25-OH-D3",
                "FER",
                "CAL",
                "MOP/MET/KET",
            )
        ),
        Category(
            title = "Inflammation",
            items = listOf<String>(
                "hsCRP/CRP",
                "PCT",
                "SAA",
                "IL-6",
                "Anti-CCP",
            )
        ),
        Category(
            title = "Renal Function",
            items = listOf<String>(
                "HbA1C",
            )
        ),
        Category(
            title = "Hormone",
            items = listOf<String>(
                "β-hCG",
                "TSH",
                "T3",
                "T4",
                "LH",
                "AMH",
                "FSH",
                "PRL",
                "Progesterone",
                "Testosterone",
                "fT4"
            )
        ),
        //third column
        Category(
            title = "Cardiac Markers",
            items = listOf<String>(
                "cTnI",
                "CK-MB",
                "D-Dimer",
                "MYO",
                "H-FABP",
                "NT-proBNP",
                "cTnI/CK-MB/MYO",
                "cTnI/CK-MB",
                "cTnT",
                "hs-cTnI"
            )
        ),
        Category(
            title = "Tumor Markers",
            items = listOf<String>(
                "PSA"
            )
        ),
    ),
)


data object PoctEvents
data object PoctAction