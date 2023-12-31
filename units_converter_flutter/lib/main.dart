import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:units_converter_flutter/converter.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: ''),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final List<String> categories = [
    'time',
    'mass',
    'length',
    'temperature',
    'amperage',
    'velocity',
    'surface',
    'angle',
    'pressure',
  ];

  String searchQuery = '';

  @override
  Widget build(BuildContext context) {
    List<String> filteredCategories = categories
        .where((category) =>
        category.toLowerCase().contains(searchQuery.toLowerCase()))
        .toList();

    return Scaffold(
      body: Center(
        child: Container(
          color: Color(0xFFF6EDFF),
          padding: EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Padding(
                padding: const EdgeInsets.only(top: 50),
                child: SearchBar(
                  hintText: 'Search',
                  leading: const Icon(Icons.search),
                  surfaceTintColor: MaterialStateProperty.all(
                      Colors.white
                  ),
                  onChanged: (value) {
                    setState(() {
                      searchQuery = value;
                    });
                  },

                ),
              ),
              Expanded(
                child: GridView.builder(
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 3,
                    crossAxisSpacing: 8.0,
                    mainAxisSpacing: 8.0,
                  ),
                  itemCount: filteredCategories.length,
                  itemBuilder: (context, index) {
                    String iconPath =
                        'assets/icons/${filteredCategories[index].toLowerCase()}.svg';
                    return RoundedButton(
                      label: filteredCategories[index],
                      iconPath: iconPath,
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => Converter(
                              resultName: filteredCategories[index],
                            ),
                          ),
                        );
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class RoundedButton extends StatelessWidget {
  const RoundedButton({
    required this.label,
    required this.iconPath,
    required this.onPressed,
  });

  final String label;
  final String iconPath;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onPressed,
      borderRadius: BorderRadius.circular(16.0),
      child: Container(
        padding: EdgeInsets.all(16.0),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16.0),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SvgPicture.asset(
              iconPath,
              width: 40.0,
              height: 40.0,
            ),
            SizedBox(height: 8.0),
            Text(label),
          ],
        ),
      ),
    );
  }
}
