---------------
    Carros
---------------

* master

    - Contém o projeto dos carros finalizado.

---------------
    Branches
---------------

* 01-nav_drawer

    - Mostra o template com Navigation Drawer

* 03-toolbar

    - Adicionado o arquivos include_toolbar.xml
    - Criado a activity CarrosActivity com toolbar (@style/AppTheme.NoActionBar)
    - Explicado action bar vs toolbar

* 04-lista-carros

    - Criada a lista de carros com RecyclerView
    - Explicação sobre o Adapter
    - Criada a enum TipoCarro (Clássicos, Esportivos e Luxo)

* 05-lista-carros-parte2

    - Mostra como passar o tipo do carro como parâmetro para a CarrosActivity
    - Dicas sobre Kotlin Android Extension no adapter

* 06-carro-detalhes

    - Criada a activity CarroActivity com o layout dos detalhes do carro
    - Feito a navegação de tela ao clicar no carro dentro da lista

* 07-parcelable

    - Explicado a interface Parcelable

* 08-fragments

    - Criada a classe CarrosFragment com o componente com a lista de carros

* 09-tabs

    - Criada a Tab com os 3 tipos de carros (Clássicos, Esportivos, Luxo)

* 10-webview

    - Criada a WebViewActivity para carregar uma página da internet
    - Exemplo com ProgressBar
    - Mostrado o gesto de "pull to refresh" com SwipeRefreshLayout

* 11-cardview

    - Adicionado um CardView na lista de carros

* 12-xml

    - Parser de XML
    - A lista de carros será criada a partir de um XML embarcado no app
    - Foi usado uma extensão para facilitar o parser de XML
    - Adicionado a descrição do carro na tela de detalhes e mostrado o ScrollView

* 13-json

    - Parser de JSON
    - Foi utilizado a lib GSON para o parser
    - Foi usado uma extensão para facilitar o parser de JSON

* 14-threads

    - Alterado para buscar os carros de um web service
    - Explicado como utilizar o básico de threads e UI Thread

* 15-picasso-extension

    - Mostrado a "extension" para utilizar a lib Picasso

* 16-anko-thread-exceptions

    - Alterado para utilizar a extensões doAsync e uiThread da lib Anko
    - Tratamento de exceções
    - Verificar se existe internet

* 17-prefs

    - Mostrado como usar a SharedPreferences para salvar dados simples de chave e valor

*  18-config

    - Mostrado como criar telas de configurações no Android

* 19-room

    - Criada a 4a tab do aplicativo chamada Favoritos
    - Vamos salvar os carros no banco de dados com a lib Room
    - Os carros salvos  vão aparecer na tab de Favoritos

* 20-fragments

    - Dicas sobre ciclo de vida do fragment
    - onResume vs onActivityCreated
    - Adicionando botões na Action Bar

* 21-bus

    - Aprenda a usar Bus de Eventos

* 22-material-design-list

    - FloatingActionButton (FAB) e CoordinatorLayout
    - Scroll Flexível na tela de lista de carros

* 23-material-design-carro

    - Scroll Flexível na tela de detalhes do carro
    - Explicado o arquivo dimens.xml para as contantes com a foto do carro

* 24-video

    - Player nativo com startActivity(intent) vs VideView

* 25-long-click

    - Adicionado o OnLongClickListener no adapter do carro

* 26-ok-http

    - Lib OkHttp
    - Alterado busca da lista de carros para usar classe HttpHelper com GET

* 27-salvar-carro

    - Salvar um carro no servidor com POST
    - Vamos enviar um evento (bus) para a tela da lista para atualizá-la depois de inserir/atualizar um carro

* 28-deletar-carro

    - Deletar um carro do servidor com uma request do tipo DELETE

* 29-long-click-alert-list

    - Mostrar um alerta para Editar/Deletar o carro ao fazer long click na lista

* 30-progress-dialog

    - Explicação sobre o ProgressDialog

* 31-editar-carro

    - Ao selecionar um carro da lista, podemos editar os dados

* 32-editar-carro-material

    - Tela de editar o carro com Material Design

* 33-cab

    - ActionBar de Context (CAB) para selecionar vários carros na lista e fazer alguma ação (deletar/compartilhar)
    - Ação para excluir vários carros do servidor e favoritos

* 34-share

    - Adicionado opção para compartilhar carros

* 35-camera

    - Tirar foto do carro

* 36-maps

    - Google Maps

* 37-firebase

    - Firebase

* 38-firebase-GA

    - Firebase GA