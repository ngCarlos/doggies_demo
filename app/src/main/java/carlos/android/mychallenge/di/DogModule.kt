package carlos.android.mychallenge.di

import carlos.android.mychallenge.data.DogsDataSource
import carlos.android.mychallenge.data.dao.DogsDatabase
import carlos.android.mychallenge.domain.DogsRepository
import carlos.android.mychallenge.viewmodel.DogsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dogModule = module {
    single { DogsDatabase.createDatabase(get()) }
    factory { get<DogsDatabase>().dao() }
    single<DogsRepository> { DogsDataSource(get()) }
    viewModel { DogsListViewModel(get()) }
}

private const val TAG = "DOGS"