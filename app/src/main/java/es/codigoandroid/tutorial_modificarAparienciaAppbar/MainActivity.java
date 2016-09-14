package es.codigoandroid.tutorial_modificarAparienciaAppbar;


import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    //Log
    private final String TAG = getClass().getSimpleName();


    private Adaptador_ViewPagerPrincipal Adaptador_ViewPagerPrincipal;
    private ViewPager ViewPager;

    private TextView titulo,subtitulo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciamos la barra de herramientas.
        final Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarPrincipal);
        setSupportActionBar(toolbar);

        final AppBarLayout appbar = (AppBarLayout) findViewById(R.id.AppbarPrincipal);
        final ColorDrawable appBarBackground = new ColorDrawable();

        // El efecto de mezclar colores solo funciona con JellyBean y superior. API 16
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            appbar.setBackground(appBarBackground);
        }

        // Recuperamos los colores que hemos configurado en nuestro arrays.xml para el efecto mezcla del cambio de Fragmento
        TypedArray arrayColoresFragmentosAppbar = getResources().obtainTypedArray(R.array.colores_fragmentos_appbar);
        TypedArray arrayColoresFragmentosTextos = getResources().obtainTypedArray(R.array.colores_fragmentos_textos);

        final int[] coloresFragmentosAppbar = new int[arrayColoresFragmentosAppbar.length()];
        final int[] coloresFragmentosTextos = new int[arrayColoresFragmentosAppbar.length()];
        for (int i = 0; i < arrayColoresFragmentosAppbar.length(); i++) {
            coloresFragmentosAppbar[i] = arrayColoresFragmentosAppbar.getColor(i, 0);
            //usamos el mismo bucle porque sabemos que tenemos la misma cantidad
            coloresFragmentosTextos[i] = arrayColoresFragmentosTextos.getColor(i, 0);
        }
        arrayColoresFragmentosAppbar.recycle();
        arrayColoresFragmentosTextos.recycle();


        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        titulo = (TextView) findViewById(R.id.textview_toolbar_titulo);
        subtitulo = (TextView) findViewById(R.id.textview_toolbar_subtitulo);


        // Iniciamos la barra de tabs
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.TabLayoutPrincipal);

        // A�adimos las 3 tabs de las secciones.
        // Le damos modo "fixed" para que todas las tabs tengan el mismo tama�o. Tambi�n le asignamos una gravedad centrada.

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());


        // Iniciamos el viewPager.
        ViewPager = (ViewPager) findViewById(R.id.ViewPagerPrincipal);
        // Creamos el adaptador, al cual le pasamos por par�mtro el gestor de Fragmentos y muy importante, el n� de tabs o secciones que hemos creado.
        Adaptador_ViewPagerPrincipal = new Adaptador_ViewPagerPrincipal(getSupportFragmentManager(), tabLayout.getTabCount(), this);
        // Y los vinculamos.
        ViewPager.setAdapter(Adaptador_ViewPagerPrincipal);

        // Y por ultimo, vinculamos el viewpager con el control de tabs para sincronizar ambos.
        tabLayout.setupWithViewPager(ViewPager);

        ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                // IMPORTANTE: Comprobamos si estamos en la última pestaña para no incurrir en un ArrayIndexOutOfBoundsException
                if (position >= Adaptador_ViewPagerPrincipal.getCount() - 1)  {
                    return;
                }
                //recuperamos los colores correspondientes
                int desdeColor = coloresFragmentosAppbar[position];
                int haciaColor = coloresFragmentosAppbar[position+1];

                int desdeColorTexto = coloresFragmentosTextos[position];
                int haciaColorTexto = coloresFragmentosTextos[position+1];

                //mezclamos los colores y actualizamos el color de fondo de la appBar y los textos de las Tabs
                final int mezclaFondo = mezclaColores(haciaColor, desdeColor, positionOffset);
                final int mezclaTexto = mezclaColores(haciaColorTexto, desdeColorTexto, positionOffset);

                appBarBackground.setColor(mezclaFondo);
                tabLayout.setTabTextColors(mezclaTexto,mezclaTexto);

                //tambien podemos crear el mismo efecto en el titulo y subtitulo
                titulo.setTextColor(mezclaTexto);
                subtitulo.setTextColor(mezclaTexto);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    public static int mezclaColores(int primerColor, int segundoColor, float ratio) {
        final float ratioInverso = 1f - ratio;
        final float r = Color.red(primerColor) * ratio + Color.red(segundoColor) * ratioInverso;
        final float g = Color.green(primerColor) * ratio + Color.green(segundoColor) * ratioInverso;
        final float b = Color.blue(primerColor) * ratio + Color.blue(segundoColor) * ratioInverso;
        return Color.rgb((int) r, (int) g, (int) b);
    }

}
