package com.habitrpg.android.habitica.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.components.UserComponent
import com.habitrpg.android.habitica.helpers.MainNavigationController
import com.habitrpg.android.habitica.models.user.User
import com.habitrpg.android.habitica.ui.theme.HabiticaTheme
import com.habitrpg.android.habitica.ui.viewmodels.MainUserViewModel
import com.habitrpg.android.habitica.ui.views.CurrencyText
import com.habitrpg.common.habitica.extensions.DataBindingUtils
import javax.inject.Inject

class BirthdayActivity : BaseActivity() {
    @Inject
    lateinit var userViewModel: MainUserViewModel

    override fun getLayoutResId(): Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabiticaTheme {
                val user = userViewModel.user.observeAsState()
                BirthdayActivityView(user.value)
            }
        }
    }

    override fun injectActivity(component: UserComponent?) {
        component?.inject(this)
    }
}

@Composable
fun BirthdayTitle(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(colorResource(id = R.color.brand_50))
        )
        Image(painterResource(id = R.drawable.birthday_textdeco_left), null)
        Text(
            text,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Image(painterResource(id = R.drawable.birthday_textdeco_right), null)
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(colorResource(id = R.color.brand_50))
        )
    }
}

@Composable
fun BirthdayActivityView(user: User?) {
    val activity = LocalContext.current as? Activity
    val textColor = Color.White
    val specialTextColor = colorResource(R.color.yellow_50)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(
                Brush.verticalGradient(
                    Pair(0.0f, colorResource(id = R.color.brand_300)),
                    Pair(1.0f, colorResource(id = R.color.brand_200))
                )
            )
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Button(
            onClick = {
                if (activity != null) {
                    activity.finish()
                    return@Button
                }
                MainNavigationController.navigateBack()
            },
            colors = ButtonDefaults.textButtonColors(contentColor = textColor),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Image(
                painterResource(R.drawable.arrow_back),
                stringResource(R.string.action_back),
                colorFilter = ColorFilter.tint(
                    textColor
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()) {
            Image(painterResource(R.drawable.birthday_header), null, Modifier.padding(bottom = 8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(R.drawable.birthday_gifts), null)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 22.dp)
                ) {
                    Text(
                        stringResource(id = R.string.limited_event).toUpperCase(Locale.current),
                        fontSize = 12.sp,
                        color = specialTextColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "X to Y",
                        fontSize = 12.sp,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                // right image should be flipped
                Image(
                    painterResource(id = R.drawable.birthday_gifts),
                    null,
                    modifier = Modifier.scale(-1f, 1f)
                )
            }
            Text(
                stringResource(R.string.birthday_title_description),
                fontSize = 16.sp,
                color = specialTextColor,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 22.dp)
            )
            BirthdayTitle(stringResource(id = R.string.animated_gryphatrice_pet))
            Box(
                Modifier
                    .padding(vertical = 20.dp)
                    .size(161.dp, 129.dp)
                    .background(colorResource(R.color.brand_50), RoundedCornerShape(8.dp))
            ) {

            }
            Text(
                stringResource(R.string.limited_edition).toUpperCase(Locale.current),
                fontSize = 12.sp,
                color = specialTextColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(R.string.gryphatrice_description),
                fontSize = 16.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            val ownsGryphatrice = false
            if (ownsGryphatrice) {
                Text(
                    stringResource(R.string.thanks_for_support),
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
                HabiticaButton(
                    Color.White,
                    colorResource(R.color.brand_200),
                    {},
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(stringResource(R.string.equip))
                }
            } else {
                Text(buildAnnotatedString {
                    append("Buy for ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("")
                    }
                    append(" or ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("60 Gems")
                    }
                }, color = Color.White)
                HabiticaButton(
                    Color.White,
                    colorResource(R.color.brand_200),
                    {},
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(stringResource(R.string.buy_for_x, ""))
                }
                HabiticaButton(
                    Color.White,
                    colorResource(R.color.brand_200),
                    {},
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.buy_for))
                        CurrencyText(currency = "gems", value = 60)
                    }
                }
            }
            BirthdayTitle(stringResource(id = R.string.plenty_of_potions))
            Text(
                stringResource(R.string.plenty_of_potions_description),
                fontSize = 16.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            PotionGrid()
            HabiticaButton(
                Color.White,
                colorResource(R.color.brand_200),
                {},
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(stringResource(R.string.visit_the_market))
            }
            BirthdayTitle(stringResource(id = R.string.for_for_free))
            Text(
                stringResource(R.string.for_for_free_description),
                fontSize = 16.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 20.dp)
                .background(colorResource(R.color.brand_50))
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 60.dp)
        ) {
            Text(
                stringResource(R.string.limitations),
                fontSize = 16.sp,
                color = colorResource(R.color.brand_600),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                stringResource(R.string.birthday_limitations),
                fontSize = 14.sp,
                color = colorResource(R.color.brand_600),
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PotionGrid() {
    val potions = listOf(
        "Porcelain",
        "Vampire",
        "Aquatic",
        "StainedGlass",
        "Celestial",
        "Glow",
        "AutumnLeaf",
        "SandSculpture",
        "Peppermint",
        "Shimmer"
    ).windowed(4, 4, true)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 20.dp)) {
        for (potionGroup in potions) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (potion in potionGroup) {
                    Box(Modifier.size(68.dp).background(colorResource(R.color.brand_50), RoundedCornerShape(8.dp))) {
                        AsyncImage(model = DataBindingUtils.BASE_IMAGE_URL + DataBindingUtils.getFullFilename("Pet_HatchingPotion_$potion"), null, Modifier.size(68.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HabiticaButton(
    background: Color,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier
        .background(background, HabiticaTheme.shapes.medium)
        .clickable { onClick() }
        .fillMaxWidth()
        .padding(8.dp)) {
        ProvideTextStyle(
            value = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        ) {
            content()
        }
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    BirthdayActivityView(null)
}