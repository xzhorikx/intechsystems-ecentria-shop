package alex.zhurkov.intechsystems_shop.common.arch

interface UIAction

interface UIStateChange

interface UIState

interface UIModel

interface UIEvent

interface Reducer<S : UIState, C : UIStateChange> {
    fun reduce(state: S, change: C): S
}

interface StateToModelMapper<S : UIState, M : UIModel> {
    fun mapStateToModel(state: S): M
}
