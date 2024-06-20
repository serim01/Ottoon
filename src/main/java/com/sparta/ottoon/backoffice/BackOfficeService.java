package com.sparta.ottoon.backoffice;

import com.sparta.ottoon.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class BackOfficeService {
    private final UserRepository userRepository;
//    private final PostRepository postRepository;


}
