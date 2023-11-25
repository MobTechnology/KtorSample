package com.ktor.bal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ktor.bal.model.InboxPostData
import com.ktor.bal.model.TabItems
import com.ktor.bal.ui.theme.KtorSampleTheme
import com.ktor.bal.viewmodel.MainViewModel
import java.util.ArrayList

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = MainViewModel()
        setContent {
            KtorSampleTheme {
                InboxListView(viewModel)
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
    @Composable
    private fun InboxListView(viewModel: MainViewModel) {
        LaunchedEffect(Unit, block = {
            val inboxPostData =
                InboxPostData(
                    InspectionIds = ArrayList(
                        listOf(
                            13917,
                            13918,
                            13919,
                            14069,
                            14070,
                            14071,
                            14072,
                            14073,
                            14074,
                            14075,
                            14076,
                            14077,
                            14078,
                            14079
                        )
                    )
                )
            viewModel.getInboxResponse(inboxPostData)
        })

        //remember navController so it does not get recreated on recomposition
        val navController = rememberNavController()

        val pagerState = rememberPagerState(0)
        val scaffoldState = rememberScaffoldState()

        Scaffold(
            topBar = {
                TopBar()
            },
            bottomBar = {
                //NavHostContainer(navController = navController, padding = PaddingValues(8.dp), viewModel = viewModel)
                BottomNavigationBar(navController = navController, viewModel)
            },
            content = { padding ->
                HorizontalPager(
                    state = pagerState,
                    count = TabItems.navItems.size,
                    modifier = Modifier.padding(padding)
                )
                { page ->
                    if (TabItems.navItems[page].title == "Case") {
                        CasesScreen(viewModel)
                    } else if (TabItems.navItems[page].title == "Inspections") {
                        InspectionScreen(viewModel)
                    } else if (TabItems.navItems[page].title == "ServiceRequests") {
                        ServiceRequestScreen(viewModel)
                    } else if (TabItems.navItems[page].title == "WorkOrders") {
                        WorkOrderScreen(viewModel)
                    } else {
                        CasesScreen(viewModel)
                    }
                }
            }
        )
    }

    /**
     * It receives navcontroller to navigate between screens,
     * padding values -> Since BottomNavigation has some heights,
     * to avoid clipping of screen, we set padding provided by scaffold
     */
    @Composable
    fun NavHostContainer(
        navController: NavHostController,
        padding: PaddingValues,
        viewModel: MainViewModel,
        page: Int
    ) {
        NavHost(
            navController = navController,

            //set the start destination as home
            startDestination = TabItems.navItems[page].route,

            //Set the padding provided by scaffold
            modifier = Modifier.padding(paddingValues = padding),

            builder = {

                //  route : Home
                if (TabItems.navItems[page].route == TabItems.Cases.route) {
                    composable(TabItems.Cases.route) {
                        CasesScreen(viewModel = viewModel)
                    }
                } else if (TabItems.navItems[page].route == TabItems.Inspection.route) {
                    //  route : search
                    composable(TabItems.Inspection.route) {
                        InspectionScreen(viewModel = viewModel)
                    }
                } else if (TabItems.navItems[page].route == TabItems.ServiceRequest.route) {
                    //  route : profile
                    composable(TabItems.ServiceRequest.route) {
                        ServiceRequestScreen(viewModel = viewModel)
                    }
                } else if (TabItems.navItems[page].route == TabItems.WorkOrder.route) {
                    composable(TabItems.WorkOrder.route) {
                        WorkOrderScreen(viewModel = viewModel)
                    }
                }
            })
    }

    /**
     * It receives navcontroller to navigate between screens,
     */

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomNavigationBar(navController: NavHostController, viewModel: MainViewModel) {
        val ctx = LocalContext.current
        //val navController = rememberNavController()
        BottomNavigation(
            //set background color
            backgroundColor = colorResource(id = R.color.asset_action_text),
            contentColor = Color.White,

            content = {
                //observe the backstack
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                //observe current route to change the icon color,label color when navigated
                val currentRoute = navBackStackEntry?.destination

                //Bottom nav items we declared
                TabItems.navItems.forEach { navItem ->

                    //Place the bottom nav items
                    BottomNavigationItem(
                        //it currentRoute is equal then its selected route
                        selected = currentRoute?.hierarchy?.any { it.route == navItem.title } == true,
                        selectedContentColor = colorResource(id = R.color.asset_action_text),
                        unselectedContentColor = colorResource(id = R.color.background),

                        //navigate on click
                        onClick = {
                            navController.navigate(navItem.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        },
                        //Icon of navItem
                        icon = {
                            getBitmapFromImage(ctx, navItem.icon)?.asImageBitmap()
                                ?.let { Icon(bitmap = it, contentDescription = navItem.title) }
                        },
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                            .align(Alignment.CenterVertically),
                        //label
                        label = {
                            Text(text = navItem.title)
                        },
                        alwaysShowLabel = false
                    )
                }
            })
        /*   val navController = rememberNavController()
           Scaffold(
               modifier = Modifier.background(Color(R.color.asset_action_text)),
               bottomBar = {
                   BottomNavigation {
                       //set background color
                       //backgroundColor = colorResource(id = R.color.asset_action_text)
                       //contentColor = Color.White

                       val navBackStackEntry by navController.currentBackStackEntryAsState()
                       val currentDestination = navBackStackEntry?.destination
                       tabs.forEach { screen ->
                           BottomNavigationItem(
                               icon = {
                                   getBitmapFromImage(ctx, screen.icon)?.asImageBitmap()
                                       ?.let { Icon(bitmap = it, contentDescription = screen.title) }
                               },
                               modifier = Modifier.height(20.dp).width(20.dp).align(Alignment.CenterVertically),

                               label = { Text(screen.title) },
                               selected = currentDestination?.hierarchy?.any { it.route == screen.title } == true,
                               onClick = {
                                   navController.navigate(screen.title) {
                                       // Pop up to the start destination of the graph to
                                       // avoid building up a large stack of destinations
                                       // on the back stack as users select items
                                       popUpTo(navController.graph.findStartDestination().id) {
                                           saveState = true
                                       }
                                       // Avoid multiple copies of the same destination when
                                       // reselecting the same item
                                       launchSingleTop = true
                                       // Restore state when reselecting a previously selected item
                                       restoreState = true
                                   }
                               }
                           )
                       }
                   }
               }
           ) { padding ->
               //NavHost(navController, startDestination = Screen.Profile.route, Modifier.padding(innerPadding)) {
               //    composable(Screen.Profile.route) { Profile(navController) }
               //     composable(Screen.FriendsList.route) { FriendsList(navController) }
               // }

               NavHostContainer(
                   navController = navController,
                   padding = PaddingValues(8.dp),
                   viewModel = viewModel
               )
           }*/
    }

    // on below line we are creating a function to get bitmap
// from image and passing params as context and an int for drawable.
    private fun getBitmapFromImage(context: Context, drawable: Int): Bitmap? {

        // on below line we are getting drawable
        val db = ContextCompat.getDrawable(context, drawable)

        // in below line we are creating our bitmap and initializing it.
        val bit = Bitmap.createBitmap(
            db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        // on below line we are
        // creating a variable for canvas.
        val canvas = Canvas(bit)

        // on below line we are setting bounds for our bitmap.
        db.setBounds(0, 0, canvas.width, canvas.height)

        // on below line we are simply
        // calling draw to draw our canvas.
        db.draw(canvas)

        // on below line we are
        // returning our bitmap.
        return bit
    }

    @Preview
    @Composable
    fun LoadBottomBar() {
        val navController = rememberNavController()
        //BottomNavigationBar(navController = navController, viewModel = viewModel)
    }

    @Composable
    fun InboxView(viewModel: MainViewModel) {

        Column(modifier = Modifier.padding(top = 60.dp)) {
            LazyColumn(modifier = Modifier.fillMaxHeight(),
                content = {
                    items(viewModel.inboxList) { item ->
                        CardView(
                            title = item.InspTemplateName,
                            date = item.InitiateDate,
                            initiatedBy = item.InitiatedBy,
                            status = item.Status
                        )
                    }
                })
        }
    }

    @Composable
    fun CardView(title: String, date: String, initiatedBy: String, status: String) {
        Column(
            modifier = Modifier
                .clip(shape = RectangleShape)
                .padding(2.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            ) {
                Column() {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Text(
                        text = initiatedBy,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
            Divider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.asset_action_text)
            )
    ) {
        SearchBar()
    }
}

@Composable
fun SearchBar() {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .height(56.dp)
            .background(
                color = colorResource(id = R.color.buttonBackgroundDark),
                RoundedCornerShape(8.dp)
            )
    ) {

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray,
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(1.5f)
        )

        Text(
            text = "Search",
            color = Color.Gray,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .weight(7f)
                .padding(start = 16.dp)
        )
    }
}