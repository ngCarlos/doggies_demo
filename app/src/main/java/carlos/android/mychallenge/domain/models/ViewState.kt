package carlos.android.mychallenge.domain.models

import carlos.android.mychallenge.domain.EventType

sealed class ViewState {
    object LoadingState: ViewState()
    data class DogsResults(val dogsList: List<Dog>): ViewState()
    data class DogData(val dog: Dog): ViewState()
    data class EventTypeState(val eventType: EventType): ViewState()
    object EmptyState: ViewState()
}