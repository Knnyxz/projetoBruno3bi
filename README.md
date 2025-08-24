# 📱 Aplicativo OCR - Leitor de Texto

Um aplicativo Android que utiliza reconhecimento óptico de caracteres (OCR) para extrair texto de imagens capturadas pela câmera ou selecionadas da galeria, com funcionalidade de leitura em voz alta.

## 🚀 Funcionalidades

### ✨ Principais Recursos
- **Captura de Imagem**: Tire fotos diretamente pelo aplicativo
- **Seleção da Galeria**: Escolha imagens já salvas no dispositivo
- **OCR Avançado**: Reconhecimento de texto usando Firebase ML Kit
- **Text-to-Speech**: Leitura em voz alta do texto reconhecido
- **Visualização de Imagem**: Exibe a imagem selecionada para referência
- **Interface Intuitiva**: Design moderno e fácil de usar

### 🔧 Funcionalidades Técnicas
- Gerenciamento inteligente de permissões (câmera e armazenamento)
- Suporte para Android 13+ com permissões de mídia atualizadas
- Tratamento de erros robusto
- Interface responsiva com ConstraintLayout

## 📋 Pré-requisitos

- Android Studio Arctic Fox ou superior
- SDK mínimo: Android 7.0 (API 24)
- SDK alvo: Android 14 (API 34)
- Dispositivo com câmera (para captura de fotos)

## 🛠️ Instalação

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/Knnyxz/projetoBruno3bi
   cd projetoBruno3
   ```

2. **Abra no Android Studio**:
   - File → Open → Selecione a pasta do projeto

3. **Sincronize as dependências**:
   - O Gradle irá baixar automaticamente todas as dependências

4. **Execute o aplicativo**:
   - Conecte um dispositivo Android ou use um emulador
   - Clique em "Run" ou pressione Shift+F10

## 📱 Como Usar

### 1. Primeira Execução
- O aplicativo solicitará permissões para câmera e armazenamento
- Conceda as permissões necessárias

### 2. Capturar Imagem
- Toque no botão "Câmera"
- Tire uma foto do texto que deseja reconhecer
- A imagem aparecerá na tela e o texto será extraído automaticamente

### 3. Selecionar da Galeria
- Toque no botão "Galeria"
- Escolha uma imagem da sua galeria
- A imagem será exibida e o texto extraído

### 4. Ouvir o Texto
- Após o reconhecimento, toque em "Ler Texto em Voz Alta"
- O aplicativo lerá o texto reconhecido

## 🏗️ Arquitetura do Projeto

### Estrutura de Arquivos
```
app/
├── src/main/
│   ├── java/com/example/projetobruno3/
│   │   └── MainActivity.kt              # Atividade principal
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml        # Layout da interface
│   │   └── values/
│   │       ├── strings.xml              # Strings do aplicativo
│   │       └── themes.xml               # Temas e estilos
│   └── AndroidManifest.xml              # Configurações e permissões
└── build.gradle.kts                     # Dependências e configurações
```

### Componentes Principais

#### MainActivity.kt
- **Gerenciamento de Permissões**: Verifica e solicita permissões necessárias
- **Launchers**: Gerencia captura de câmera e seleção de galeria
- **Processamento OCR**: Integração com Firebase ML Kit
- **Text-to-Speech**: Implementação de leitura em voz alta
- **Interface**: Controle de todos os elementos da UI

#### activity_main.xml
- **Layout Responsivo**: ConstraintLayout para diferentes tamanhos de tela
- **Componentes UI**: Botões, ImageView, TextView e ScrollView
- **Design Moderno**: Interface limpa e intuitiva

## 🔧 Dependências Utilizadas

### Firebase ML Kit
```kotlin
implementation 'com.google.mlkit:text-recognition:16.0.0'
```
- Reconhecimento óptico de caracteres
- Suporte para texto em latim
- Processamento local (offline)

### Android Components
- **Activity Result API**: Gerenciamento moderno de resultados
- **ConstraintLayout**: Layout flexível e responsivo
- **TextToSpeech**: Síntese de voz nativa do Android

## 🔐 Permissões

### Permissões Declaradas
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

### Gerenciamento Inteligente
- **Android 13+**: Usa `READ_MEDIA_IMAGES` para acesso a imagens
- **Android 12 e anterior**: Usa `READ_EXTERNAL_STORAGE`
- **Verificação Runtime**: Solicita permissões conforme necessário

## 🐛 Solução de Problemas

### Permissões Negadas
**Problema**: "Permissão de armazenamento negada"
**Solução**:
1. Vá em Configurações → Apps → [Nome do App] → Permissões
2. Ative as permissões de Câmera e Armazenamento/Fotos
3. Reinicie o aplicativo

### Erro de OCR
**Problema**: Texto não é reconhecido
**Solução**:
- Certifique-se de que o texto está claro e bem iluminado
- Evite imagens muito pequenas ou com baixa resolução
- Tente diferentes ângulos de captura

### Problemas de TTS
**Problema**: Texto não é lido em voz alta
**Solução**:
- Verifique se o volume do dispositivo está ligado
- Confirme se há um mecanismo TTS instalado no sistema
- Reinicie o aplicativo

## 👨‍💻 Desenvolvedor

@Knnyxz

---

**Versão**: 1.0.0  
**Última atualização**: Janeiro 2025  
**Compatibilidade**: Android 7.0+
