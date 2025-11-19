// Остальные импорты
import io.hexlet.spring.mapper.PostMapper;

@RestController
@RequestMapping("/api")
public class PostsController {
    @Autowired
    private PostRepository repository;

    @Autowired
    private PostMapper postMapper;

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@RequestBody PostCreateDTO postData) {
        // Преобразование в сущность
        var post = postMapper.map(postData);
        repository.save(post);
        // Преобразование в DTO
        var postDTO = postMapper.map(post);
        return postDTO;
    }

    @PutMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO update(@RequestBody @Valid PostUpdateDTO postData, @PathVariable Long id) {
        var post = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        postMapper.update(postData, post);
        repository.save(post);
        var postDTO = postMapper.map(post);
        return postDTO;
    }

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO show(@PathVariable Long id) {
        var post = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        // Преобразование в DTO
        var postDTO = postMapper.map(post);
        return postDTO;
    }
}
